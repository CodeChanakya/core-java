package com.codechanakya.asyncpay;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * One payment's journey, written as a chain of steps that run when the previous one finishes.
 *
 * <pre>
 * validate -> ( fraud score  +  balance check ) -> authorise -> notify -> result
 *                  in parallel, then combined
 * </pre>
 *
 * <p>The method returns at once with a {@link CompletableFuture}. Nothing blocks. Each arrow above
 * is {@code thenApply}, {@code thenCompose} or {@code thenCombine}, and the error handling sits at
 * the very end in one {@code handle} call, so no stage needs its own try and catch.
 */
public final class PaymentPipeline {

    private static final long GATEWAY_DEADLINE_MS = 400;
    private static final long NOTICE_DEADLINE_MS = 200;

    private final Executor executor;
    private final FraudService fraud;
    private final BalanceService balances;
    private final GatewayService gateway;
    private final NotificationService notifier;

    public PaymentPipeline(Executor executor, FraudService fraud, BalanceService balances,
                           GatewayService gateway, NotificationService notifier) {
        this.executor = executor;
        this.fraud = fraud;
        this.balances = balances;
        this.gateway = gateway;
        this.notifier = notifier;
    }

    /** What the two parallel checks found. */
    private record Checks(int fraudScore, boolean hasFunds) {
    }

    /** Starts the payment and returns straight away. The future always completes normally, with a {@link PaymentResult}. */
    public CompletableFuture<PaymentResult> process(PaymentRequest request) {
        // Several stages run on different threads, one after another. A copy-on-write list is
        // safe to add to from whichever thread runs the next stage.
        List<String> trail = new CopyOnWriteArrayList<>();
        trail.add("PENDING");

        return CompletableFuture
                .supplyAsync(() -> validate(request, trail), executor)
                .thenCompose(valid -> checkInParallel(valid, trail))
                .thenCompose(valid -> authorise(valid, trail))
                .thenCompose(code -> tellTheCustomer(request, code, trail))
                .handle((detail, error) -> error == null
                        ? new PaymentResult(request.id(), Status.COMPLETED, detail, trail)
                        : failure(request, error, trail));
    }

    private PaymentRequest validate(PaymentRequest request, List<String> trail) {
        if (request.amount().signum() <= 0) {
            throw new InvalidPaymentException("amount must be positive");
        }
        if (!List.of("card", "upi", "wallet").contains(request.method())) {
            throw new InvalidPaymentException("unknown method " + request.method());
        }
        trail.add("VALIDATED");
        return request;
    }

    /**
     * Starts the fraud check and the balance check together, waits for both, and applies the rules.
     * {@code thenCombine} is the "both of these, then that" operation.
     */
    private CompletableFuture<PaymentRequest> checkInParallel(PaymentRequest request, List<String> trail) {
        return fraud.score(request)
                .thenCombine(balances.hasFunds(request), Checks::new)
                .thenApply(checks -> {
                    if (!checks.hasFunds()) {
                        throw new DeclinedException("insufficient funds");
                    }
                    if (checks.fraudScore() >= 80) {
                        throw new DeclinedException("fraud score " + checks.fraudScore() + " is too high");
                    }
                    trail.add("CHECKED");
                    return request;
                });
    }

    /**
     * The gateway call is the one we cannot trust to answer, so it gets a deadline.
     * {@code orTimeout} makes the future fail with a {@link TimeoutException} if it is still empty after the time.
     */
    private CompletableFuture<String> authorise(PaymentRequest request, List<String> trail) {
        return gateway.authorize(request)
                .orTimeout(GATEWAY_DEADLINE_MS, TimeUnit.MILLISECONDS)
                .thenApply(code -> {
                    trail.add("AUTHORISED");
                    return code;
                });
    }

    /**
     * The money has moved, so a slow mail server must not turn a good payment into a bad one.
     * {@code completeOnTimeout} supplies a default instead of an error.
     */
    private CompletableFuture<String> tellTheCustomer(PaymentRequest request, String code, List<String> trail) {
        return notifier.send(request)
                .completeOnTimeout("NOT SENT", NOTICE_DEADLINE_MS, TimeUnit.MILLISECONDS)
                .thenApply(notice -> {
                    trail.add("COMPLETED");
                    return "authorised " + code + ", notice " + notice;
                });
    }

    /** Turns whatever went wrong into a result. Exceptions in a chain arrive wrapped, so we unwrap first. */
    private PaymentResult failure(PaymentRequest request, Throwable error, List<String> trail) {
        Throwable cause = error;
        while ((cause instanceof CompletionException || cause instanceof ExecutionException) && cause.getCause() != null) {
            cause = cause.getCause();
        }

        Status status;
        String detail;
        if (cause instanceof DeclinedException) {
            status = Status.DECLINED;
            detail = "Declined: " + cause.getMessage();
        } else if (cause instanceof InvalidPaymentException) {
            status = Status.FAILED;
            detail = "Invalid payment: " + cause.getMessage();
        } else if (cause instanceof GatewayUnavailableException) {
            status = Status.FAILED;
            detail = "Gateway unavailable, try again later";
        } else if (cause instanceof TimeoutException) {
            status = Status.TIMED_OUT;
            detail = "No answer from the gateway within " + GATEWAY_DEADLINE_MS + " ms";
        } else {
            status = Status.FAILED;
            detail = "Unexpected " + cause.getClass().getSimpleName();
        }
        trail.add(status.name());
        return new PaymentResult(request.id(), status, detail, trail);
    }
}

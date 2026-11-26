package com.codechanakya.asyncpay;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Asynchronous Payment Status Simulator, the project of Phase 8.
 *
 * <p>Many payments are in flight at once. Each one is a chain of {@link CompletableFuture} steps,
 * and the program prints only after the futures are joined, in the order the payments were
 * started, so the output is the same on every run no matter which thread finishes first.
 */
public class Main {

    private static final PaymentRequest HAPPY = PaymentRequest.of("P-1001", "ACC-A", "2500", "card");
    private static final PaymentRequest LOW_FUNDS = PaymentRequest.of("P-2001", "ACC-B", "4000", "upi");
    private static final PaymentRequest RISKY = PaymentRequest.of("P-2002", "ACC-A", "95000", "card");
    private static final PaymentRequest ZERO = PaymentRequest.of("P-2003", "ACC-A", "0", "card");
    private static final PaymentRequest GATEWAY_DOWN = PaymentRequest.of("P-GATE", "ACC-A", "1200", "wallet");
    private static final PaymentRequest GATEWAY_SLOW = PaymentRequest.of("P-SLOW", "ACC-A", "1800", "card");
    private static final PaymentRequest SLOW_MAIL = PaymentRequest.of("P-MAIL", "ACC-A", "700", "wallet");

    public static void main(String[] args) throws Exception {
        try (PaymentSystem system = new PaymentSystem()) {
            onePaymentStepByStep(system);
            sevenWaysToEnd(system);
            aBatchAtOnce(system);
            firstAnswerWins(system);
            tryingAgain(system);
        }
    }

    private static void onePaymentStepByStep(PaymentSystem system) {
        System.out.println("--- One payment, step by step ---");
        CompletableFuture<PaymentResult> future = system.pipeline().process(HAPPY);
        System.out.println("  process() has returned. It did not wait for the payment to finish.");
        PaymentResult result = future.join();                        // now wait for it
        System.out.println("  " + result.id() + " " + result.status() + ": " + result.detail());
        System.out.println("  Trail: " + result.trailText());
    }

    private static void sevenWaysToEnd(PaymentSystem system) {
        System.out.println();
        System.out.println("--- Seven payments started together ---");
        List<PaymentRequest> requests = List.of(HAPPY, LOW_FUNDS, RISKY, ZERO, GATEWAY_DOWN, GATEWAY_SLOW, SLOW_MAIL);

        List<CompletableFuture<PaymentResult>> running = new ArrayList<>();
        for (PaymentRequest request : requests) {
            running.add(system.pipeline().process(request));        // all seven start now
        }
        for (CompletableFuture<PaymentResult> future : running) {
            PaymentResult result = future.join();
            System.out.println(String.format(Locale.ROOT, "  %-7s %-10s %s", result.id(), result.status(), result.detail()));
            System.out.println("          " + result.trailText());
        }
    }

    private static void aBatchAtOnce(PaymentSystem system) {
        System.out.println();
        System.out.println("--- A batch of nine, waiting with allOf ---");
        List<PaymentRequest> batch = new ArrayList<>(List.of(HAPPY, LOW_FUNDS, RISKY, ZERO, GATEWAY_DOWN, GATEWAY_SLOW, SLOW_MAIL));
        batch.add(PaymentRequest.of("P-1002", "ACC-A", "300", "upi"));
        batch.add(PaymentRequest.of("P-1003", "ACC-A", "45000", "card"));

        AtomicInteger callbacks = new AtomicInteger();
        List<CompletableFuture<PaymentResult>> running = batch.stream()
                .map(request -> system.pipeline().process(request)
                        .whenComplete((result, error) -> callbacks.incrementAndGet()))   // runs as each one finishes
                .toList();

        CompletableFuture.allOf(running.toArray(new CompletableFuture[0])).join();      // a future that waits for all nine

        Map<Status, Long> byStatus = running.stream()
                .map(CompletableFuture::join)                                            // already done, so no waiting
                .collect(Collectors.groupingBy(PaymentResult::status, () -> new EnumMap<>(Status.class), Collectors.counting()));
        System.out.println("  By status: " + byStatus);
        System.out.println("  Callbacks that fired: " + callbacks.get());
    }

    private static void firstAnswerWins(PaymentSystem system) {
        System.out.println();
        System.out.println("--- Two gateways, the faster one wins ---");
        CompletableFuture<String> primary = CompletableFuture.supplyAsync(() -> {
            Latency.pause(400);
            return "primary gateway";
        }, system.executor());
        CompletableFuture<String> backup = CompletableFuture.supplyAsync(() -> {
            Latency.pause(40);
            return "backup gateway";
        }, system.executor());

        String winner = primary.applyToEither(backup, Function.identity()).join();
        System.out.println("  Answered by: " + winner);
    }

    private static void tryingAgain(PaymentSystem system) {
        System.out.println();
        System.out.println("--- Trying again when the gateway fails ---");

        AtomicInteger calls = new AtomicInteger();
        String code = Retry.withAttempts(3, () -> {
            int attempt = calls.incrementAndGet();
            return CompletableFuture.supplyAsync(() -> {
                if (attempt < 3) {
                    throw new GatewayUnavailableException("attempt " + attempt + " failed");
                }
                return "AUTH-RETRIED";
            }, system.executor());
        }).join();
        System.out.println("  Succeeded after " + calls.get() + " attempts: " + code);

        AtomicInteger doomed = new AtomicInteger();
        try {
            Retry.<String>withAttempts(3, () -> {
                doomed.incrementAndGet();
                return CompletableFuture.failedFuture(new GatewayUnavailableException("still down"));
            }).join();
        } catch (CompletionException e) {
            System.out.println("  Gave up after " + doomed.get() + " attempts: " + e.getCause().getMessage());
        }
    }
}

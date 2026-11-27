package com.codechanakya.asyncpay;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Gives each payment a risk score from 0 to 100. A real one would ask a model; this one asks the amount. */
public final class FraudService {

    private static final BigDecimal UNUSUALLY_LARGE = new BigDecimal("90000");

    private final Executor executor;

    public FraudService(Executor executor) {
        this.executor = executor;
    }

    /** Returns at once with a future; the score is worked out on another thread. */
    public CompletableFuture<Integer> score(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Latency.pause(30);
            return request.amount().compareTo(UNUSUALLY_LARGE) >= 0 ? 90 : 10;
        }, executor);
    }
}

package com.codechanakya.asyncpay;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** I1: a third check that runs alongside the other two: one payment may not exceed the daily limit. */
public final class LimitService {

    private static final BigDecimal LIMIT = new BigDecimal("50000");

    private final Executor executor;

    public LimitService(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<Boolean> withinLimit(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Latency.pause(25);
            return request.amount().compareTo(LIMIT) <= 0;
        }, executor);
    }
}

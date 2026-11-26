package com.codechanakya.asyncpay;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Says whether an account can afford a payment. */
public final class BalanceService {

    private final Map<String, BigDecimal> balances;
    private final Executor executor;

    public BalanceService(Map<String, BigDecimal> balances, Executor executor) {
        this.balances = Map.copyOf(balances);
        this.executor = executor;
    }

    public CompletableFuture<Boolean> hasFunds(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Latency.pause(20);
            return balances.getOrDefault(request.account(), BigDecimal.ZERO).compareTo(request.amount()) >= 0;
        }, executor);
    }
}

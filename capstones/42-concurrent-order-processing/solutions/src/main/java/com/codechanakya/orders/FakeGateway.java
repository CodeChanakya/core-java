package com.codechanakya.orders;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A predictable gateway. Card 0000 is declined, card ERR fails with an exception, card SLOW answers only after
 * {@code slowMillis}, and every other card is accepted straight away.
 */
public class FakeGateway implements PaymentGateway {

    private final Executor executor;
    private final long slowMillis;

    public FakeGateway(Executor executor, long slowMillis) {
        this.executor = executor;
        this.slowMillis = slowMillis;
    }

    @Override
    public CompletableFuture<Boolean> charge(String card) {
        return switch (card) {
            case "ERR" -> CompletableFuture.supplyAsync(() -> {
                throw new IllegalStateException("gateway unavailable");
            }, executor);
            case "SLOW" -> CompletableFuture.supplyAsync(() -> true,
                    CompletableFuture.delayedExecutor(slowMillis, TimeUnit.MILLISECONDS, executor));
            case "0000" -> CompletableFuture.supplyAsync(() -> false, executor);
            default -> CompletableFuture.supplyAsync(() -> true, executor);
        };
    }
}

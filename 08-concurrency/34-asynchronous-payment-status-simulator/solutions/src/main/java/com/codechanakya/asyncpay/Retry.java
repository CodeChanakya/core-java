package com.codechanakya.asyncpay;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/** Tries an asynchronous action again when it fails. */
public final class Retry {

    private Retry() {
    }

    /**
     * Runs {@code action}. If its future fails and attempts remain, runs it again, without blocking
     * any thread while waiting.
     *
     * <p>{@code handle} sees both the value and the error. We return a <em>future of a future</em>
     * from it, either the good result or the next attempt, and {@code thenCompose} flattens it.
     *
     * @param attempts the total number of tries, including the first
     */
    public static <T> CompletableFuture<T> withAttempts(int attempts, Supplier<CompletableFuture<T>> action) {
        return action.get()
                .handle((value, error) -> {
                    if (error == null) {
                        return CompletableFuture.completedFuture(value);
                    }
                    if (attempts > 1) {
                        return withAttempts(attempts - 1, action);
                    }
                    return CompletableFuture.<T>failedFuture(error);
                })
                .thenCompose(next -> next);
    }

    /** I2: waits between attempts, without blocking a thread. delayedExecutor runs the next try later. */
    public static <T> CompletableFuture<T> withDelay(int attempts, long delayMs, Supplier<CompletableFuture<T>> action) {
        return action.get()
                .handle((value, error) -> {
                    if (error == null) {
                        return CompletableFuture.completedFuture(value);
                    }
                    if (attempts > 1) {
                        return CompletableFuture.supplyAsync(() -> null,
                                        CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS))
                                .thenCompose(ignored -> withDelay(attempts - 1, delayMs, action));
                    }
                    return CompletableFuture.<T>failedFuture(error);
                })
                .thenCompose(next -> next);
    }
}

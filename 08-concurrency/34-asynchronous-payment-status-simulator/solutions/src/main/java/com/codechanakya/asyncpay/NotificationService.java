package com.codechanakya.asyncpay;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Tells the customer. {@code P-MAIL} gets a very slow mail server. */
public final class NotificationService {

    private final Executor executor;

    public NotificationService(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<String> send(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Latency.pause(request.id().equals("P-MAIL") ? 1000 : 20);
            return "sent";
        }, executor);
    }
}

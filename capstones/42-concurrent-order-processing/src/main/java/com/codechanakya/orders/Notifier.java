package com.codechanakya.orders;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/**
 * Sends confirmation messages. Here a message is only recorded, in a queue that many threads may add to at once.
 * The address {@code bad} always fails, so the order code can be tested against a broken mail server.
 */
public class Notifier {

    private final Executor executor;
    private final ConcurrentLinkedQueue<String> sent = new ConcurrentLinkedQueue<>();

    public Notifier(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<Void> send(String email, String message) {
        return CompletableFuture.runAsync(() -> {
            if ("bad".equals(email)) {
                throw new IllegalStateException("mail server refused " + email);
            }
            sent.add(email + ": " + message);
        }, executor);
    }

    /** What was sent, sorted, because the threads finish in no fixed order. */
    public List<String> sent() {
        return sent.stream().sorted().toList();
    }
}

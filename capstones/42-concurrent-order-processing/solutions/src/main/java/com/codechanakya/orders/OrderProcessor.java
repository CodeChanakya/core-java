package com.codechanakya.orders;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Processes orders without blocking: reserve stock, charge the card, send the confirmation. Each step starts
 * when the one before it finishes, and no thread waits in between.
 *
 * <p>Rules that keep it safe: stock is reserved first, because it is the scarce thing; it is given back on every
 * path that does not complete; a payment that takes too long times out; a failing confirmation never undoes a
 * paid order; and every order ends with exactly one {@link OrderResult}, whatever goes wrong.
 */
public class OrderProcessor {

    private final Inventory inventory;
    private final PaymentGateway gateway;
    private final Notifier notifier;
    private final Duration paymentTimeout;

    public OrderProcessor(Inventory inventory, PaymentGateway gateway, Notifier notifier, Duration paymentTimeout) {
        this.inventory = inventory;
        this.gateway = gateway;
        this.notifier = notifier;
        this.paymentTimeout = paymentTimeout;
    }

    public CompletableFuture<OrderResult> process(Order order) {
        if (!inventory.reserve(order.sku(), order.quantity())) {
            return CompletableFuture.completedFuture(new OrderResult(order.id(), OrderStatus.OUT_OF_STOCK,
                    "not enough stock of " + order.sku()));
        }
        return gateway.charge(order.card())
                .orTimeout(paymentTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .thenCompose(accepted -> accepted ? confirm(order) : declined(order))
                .exceptionally(error -> failed(order, error));
    }

    /** Processes every order at once and returns the results in the same order as the orders. */
    public CompletableFuture<List<OrderResult>> processAll(List<Order> orders) {
        List<CompletableFuture<OrderResult>> futures = orders.stream().map(this::process).toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(done -> futures.stream().map(CompletableFuture::join).toList());
    }

    private CompletableFuture<OrderResult> confirm(Order order) {
        return notifier.send(order.email(), "order " + order.id() + " confirmed")
                .handle((nothing, error) -> new OrderResult(order.id(), OrderStatus.COMPLETED,
                        error == null ? "paid and confirmed" : "paid, but the confirmation failed"));
    }

    private CompletableFuture<OrderResult> declined(Order order) {
        inventory.release(order.sku(), order.quantity());
        return CompletableFuture.completedFuture(new OrderResult(order.id(), OrderStatus.DECLINED, "card declined"));
    }

    private OrderResult failed(Order order, Throwable error) {
        inventory.release(order.sku(), order.quantity());
        Throwable cause = unwrap(error);
        if (cause instanceof TimeoutException) {
            return new OrderResult(order.id(), OrderStatus.TIMED_OUT, "no answer from the payment gateway");
        }
        return new OrderResult(order.id(), OrderStatus.FAILED, cause.getMessage());
    }

    /** A failure passed down a chain arrives wrapped in a CompletionException. This finds the real one. */
    static Throwable unwrap(Throwable error) {
        Throwable current = error;
        while ((current instanceof CompletionException || current instanceof ExecutionException) && current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }
}

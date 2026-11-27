package com.codechanakya.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderProcessorTest {

    private ExecutorService pool;
    private Inventory inventory;
    private Notifier notifier;
    private OrderProcessor processor;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(8);
        inventory = new Inventory(Map.of("A", 10));
        notifier = new Notifier(pool);
        processor = new OrderProcessor(inventory, new FakeGateway(pool, 3000), notifier, Duration.ofMillis(300));
    }

    @AfterEach
    void tearDown() {
        pool.shutdownNow();
    }

    private static Order order(int id, String card, int quantity) {
        return new Order(id, "A", quantity, card, "c" + id + "@example.com");
    }

    private static <T> T await(CompletableFuture<T> future) throws Exception {
        return future.get(10, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("a good order is completed, takes the stock and sends one confirmation")
    void completed() throws Exception {
        OrderResult result = await(processor.process(order(1, "4111", 3)));
        assertEquals(OrderStatus.COMPLETED, result.status());
        assertEquals(7, inventory.available("A"));
        assertEquals(List.of("c1@example.com: order 1 confirmed"), notifier.sent());
    }

    @Test
    @DisplayName("an order for more than is in stock is refused and touches nothing")
    void outOfStock() throws Exception {
        OrderResult result = await(processor.process(order(1, "4111", 11)));
        assertEquals(OrderStatus.OUT_OF_STOCK, result.status());
        assertEquals(10, inventory.available("A"));
        assertTrue(notifier.sent().isEmpty());
    }

    @Test
    @DisplayName("a declined card gives the stock back and sends nothing")
    void declined() throws Exception {
        OrderResult result = await(processor.process(order(1, "0000", 4)));
        assertEquals(OrderStatus.DECLINED, result.status());
        assertEquals(10, inventory.available("A"));
        assertTrue(notifier.sent().isEmpty());
    }

    @Test
    @DisplayName("a gateway that throws gives a FAILED order with the real message, not a wrapper's")
    void gatewayError() throws Exception {
        OrderResult result = await(processor.process(order(1, "ERR", 4)));
        assertEquals(OrderStatus.FAILED, result.status());
        assertEquals("gateway unavailable", result.detail());
        assertEquals(10, inventory.available("A"));
    }

    @Test
    @DisplayName("a payment that never answers times out, and the stock comes back")
    void timeout() throws Exception {
        OrderResult result = await(processor.process(order(1, "SLOW", 4)));
        assertEquals(OrderStatus.TIMED_OUT, result.status());
        assertEquals(10, inventory.available("A"));
    }

    @Test
    @DisplayName("a failing confirmation does not undo a paid order")
    void confirmationFails() throws Exception {
        OrderResult result = await(processor.process(new Order(1, "A", 2, "4111", "bad")));
        assertEquals(OrderStatus.COMPLETED, result.status());
        assertEquals("paid, but the confirmation failed", result.detail());
        assertEquals(8, inventory.available("A"));
    }

    @Test
    @DisplayName("processAll returns one result per order, in the order they were given")
    void processAllKeepsOrder() throws Exception {
        List<Order> orders = List.of(order(1, "4111", 1), order(2, "0000", 1), order(3, "ERR", 1), order(4, "4111", 99));
        List<OrderResult> results = await(processor.processAll(orders));
        assertEquals(List.of(1, 2, 3, 4), results.stream().map(OrderResult::orderId).toList());
        assertEquals(List.of(OrderStatus.COMPLETED, OrderStatus.DECLINED, OrderStatus.FAILED, OrderStatus.OUT_OF_STOCK),
                results.stream().map(OrderResult::status).toList());
    }

    @Test
    @DisplayName("60 buyers for 10 items: exactly 10 complete, 50 are refused, and the stock is exactly zero")
    void burst() throws Exception {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            orders.add(order(i, "4111", 1));
        }
        List<OrderResult> results = await(processor.processAll(orders));
        assertEquals(10L, results.stream().filter(r -> r.status() == OrderStatus.COMPLETED).count());
        assertEquals(50L, results.stream().filter(r -> r.status() == OrderStatus.OUT_OF_STOCK).count());
        assertEquals(0, inventory.available("A"));
    }

    @Test
    @DisplayName("stock is conserved: completed units plus what is left always equals what we started with")
    void stockIsConserved() throws Exception {
        List<Order> orders = new ArrayList<>();
        String[] cards = {"4111", "0000", "ERR", "4111", "0000"};
        for (int i = 0; i < 40; i++) {
            orders.add(order(i, cards[i % cards.length], 1 + i % 3));
        }
        List<OrderResult> results = await(processor.processAll(orders));
        int sold = results.stream().filter(r -> r.status() == OrderStatus.COMPLETED)
                .mapToInt(r -> orders.get(r.orderId()).quantity()).sum();
        assertEquals(10, sold + inventory.available("A"));
    }

    @Test
    @DisplayName("unwrap finds the real cause inside a CompletionException")
    void unwrap() {
        Throwable real = new TimeoutException("late");
        assertEquals(real, OrderProcessor.unwrap(new CompletionException(new CompletionException(real))));
        assertEquals(real, OrderProcessor.unwrap(real));
    }
}

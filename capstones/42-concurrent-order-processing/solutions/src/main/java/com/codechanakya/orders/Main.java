package com.codechanakya.orders;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Processes a batch of orders on a pool of threads. The threads finish in no fixed order, so everything printed is
 * either sorted or a count. That is how a concurrent program gets the same output on every run.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            Inventory inventory = new Inventory(Map.of("BK-101", 20, "EL-202", 3, "HM-301", 4, "TY-401", 10));
            Notifier notifier = new Notifier(pool);
            OrderProcessor processor = new OrderProcessor(inventory, new FakeGateway(pool, 1000), notifier, Duration.ofMillis(250));

            System.out.println("== Orders, all processed at the same time");
            List<Order> orders = List.of(
                    new Order(1, "BK-101", 2, "4111", "asha@example.com"),
                    new Order(2, "TY-401", 3, "5500", "bilal@example.com"),
                    new Order(3, "HM-301", 1, "0000", "chitra@example.com"),
                    new Order(4, "HM-301", 1, "ERR", "dev@example.com"),
                    new Order(5, "BK-101", 1, "SLOW", "eva@example.com"),
                    new Order(6, "TY-401", 99, "4111", "farid@example.com"),
                    new Order(7, "BK-101", 3, "4111", "bad"),
                    new Order(8, "ZZ-999", 1, "4111", "gita@example.com"));
            for (OrderResult r : processor.processAll(orders).get(10, TimeUnit.SECONDS)) {
                System.out.printf("  order %d: %-12s %s%n", r.orderId(), r.status(), r.detail());
            }

            System.out.println();
            System.out.println("== Confirmations sent for those orders (sorted, because threads finish in no fixed order)");
            notifier.sent().forEach(line -> System.out.println("  " + line));

            System.out.println();
            System.out.println("== Burst: 40 buyers for the last 3 keyboards");
            List<Order> burst = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                burst.add(new Order(100 + i, "EL-202", 1, "4111", "buyer" + i + "@example.com"));
            }
            Map<OrderStatus, Integer> counts = new EnumMap<>(OrderStatus.class);
            for (OrderResult r : processor.processAll(burst).get(10, TimeUnit.SECONDS)) {
                counts.merge(r.status(), 1, Integer::sum);
            }
            System.out.println("  " + counts + "  (which three buyers won is not fixed, but the count always is)");

            System.out.println();
            System.out.println("== Stock at the end (every failed order gave its stock back)");
            inventory.snapshot().forEach((sku, level) -> System.out.printf("  %-7s %2d%n", sku, level));
        } finally {
            pool.shutdown();
        }
    }
}

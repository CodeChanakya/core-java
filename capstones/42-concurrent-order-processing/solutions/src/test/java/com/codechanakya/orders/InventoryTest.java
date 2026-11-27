package com.codechanakya.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    @DisplayName("reserve takes the stock, and release puts it back")
    void reserveAndRelease() {
        Inventory inventory = new Inventory(Map.of("A", 10));
        assertTrue(inventory.reserve("A", 4));
        assertEquals(6, inventory.available("A"));
        inventory.release("A", 4);
        assertEquals(10, inventory.available("A"));
    }

    @Test
    @DisplayName("asking for more than is there changes nothing")
    void notEnough() {
        Inventory inventory = new Inventory(Map.of("A", 3));
        assertFalse(inventory.reserve("A", 4));
        assertEquals(3, inventory.available("A"));
    }

    @Test
    @DisplayName("the exact amount is allowed, and then the shelf is empty")
    void exactAmount() {
        Inventory inventory = new Inventory(Map.of("A", 3));
        assertTrue(inventory.reserve("A", 3));
        assertFalse(inventory.reserve("A", 1));
        assertEquals(0, inventory.available("A"));
    }

    @Test
    @DisplayName("unknown products and non-positive quantities are refused")
    void badRequests() {
        Inventory inventory = new Inventory(Map.of("A", 3));
        assertFalse(inventory.reserve("nope", 1));
        assertFalse(inventory.reserve("A", 0));
        assertFalse(inventory.reserve("A", -2));
        assertEquals(0, inventory.available("nope"));
        assertEquals(3, inventory.available("A"));
    }

    @Test
    @DisplayName("the snapshot is a sorted copy")
    void snapshot() {
        Inventory inventory = new Inventory(Map.of("B", 2, "A", 1));
        Map<String, Integer> copy = inventory.snapshot();
        assertEquals("{A=1, B=2}", copy.toString());
        copy.put("A", 999);
        assertEquals(1, inventory.available("A"));
    }

    @Test
    @DisplayName("200 threads fight for 50 items, 50 times over: exactly 50 win each time and the stock ends at zero")
    void manyThreadsNeverOversell() throws Exception {
        // A race is a matter of luck, so one round proves little. Many rounds make a lucky escape very unlikely.
        ExecutorService pool = Executors.newFixedThreadPool(16);
        try {
            for (int round = 0; round < 50; round++) {
                Inventory inventory = new Inventory(Map.of("A", 50));
                CountDownLatch start = new CountDownLatch(1);
                List<Future<Boolean>> results = new ArrayList<>();
                for (int i = 0; i < 200; i++) {
                    results.add(pool.submit(() -> {
                        start.await();                       // every thread waits here, so they all start together
                        return inventory.reserve("A", 1);
                    }));
                }
                start.countDown();
                int wins = 0;
                for (Future<Boolean> result : results) {
                    if (result.get(10, TimeUnit.SECONDS)) {
                        wins++;
                    }
                }
                assertEquals(50, wins, "round " + round);
                assertEquals(0, inventory.available("A"), "round " + round);
            }
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    @DisplayName("reserving and releasing from many threads ends where it started")
    void balancedTraffic() throws Exception {
        Inventory inventory = new Inventory(Map.of("A", 100));
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<?>> tasks = new ArrayList<>();
            for (int i = 0; i < 400; i++) {
                tasks.add(pool.submit(() -> {
                    if (inventory.reserve("A", 1)) {
                        inventory.release("A", 1);
                    }
                }));
            }
            for (Future<?> task : tasks) {
                task.get(10, TimeUnit.SECONDS);
            }
            assertEquals(100, inventory.available("A"));
        } finally {
            pool.shutdownNow();
        }
    }
}

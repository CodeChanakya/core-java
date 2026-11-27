package com.codechanakya.orders;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stock levels that many threads can change at the same time.
 *
 * <p>Each product has an {@link AtomicInteger}. {@link #reserve} reads the level, checks it, and tries to
 * swap in the lower level with {@code compareAndSet}. If another thread got there first the swap fails and
 * the loop tries again with the fresh value. So the check and the change act as one step, and stock can
 * never go below zero, without locking anything.
 */
public class Inventory {

    private final Map<String, AtomicInteger> stock = new ConcurrentHashMap<>();

    public Inventory(Map<String, Integer> initial) {
        initial.forEach((sku, quantity) -> stock.put(sku, new AtomicInteger(quantity)));
    }

    /** Takes {@code quantity} units if all of them are there. Returns false, changing nothing, if not. */
    public boolean reserve(String sku, int quantity) {
        AtomicInteger level = stock.get(sku);
        if (level == null || quantity <= 0) {
            return false;
        }
        while (true) {
            int current = level.get();
            if (current < quantity) {
                return false;
            }
            if (level.compareAndSet(current, current - quantity)) {
                return true;
            }
        }
    }

    /** Puts units back, for an order that was reserved and then did not go through. */
    public void release(String sku, int quantity) {
        stock.get(sku).addAndGet(quantity);
    }

    public int available(String sku) {
        AtomicInteger level = stock.get(sku);
        return level == null ? 0 : level.get();
    }

    /** A sorted copy of all levels. A copy, so the caller cannot change the inventory by accident. */
    public Map<String, Integer> snapshot() {
        Map<String, Integer> copy = new TreeMap<>();
        stock.forEach((sku, level) -> copy.put(sku, level.get()));
        return copy;
    }
}

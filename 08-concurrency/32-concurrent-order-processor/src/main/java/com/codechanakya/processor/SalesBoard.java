package com.codechanakya.processor;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Counts units sold per product while many threads report sales at once.
 *
 * <p>A plain {@code HashMap} is not safe for this: two threads writing at the same time can lose
 * entries or corrupt the table. {@link ConcurrentHashMap} is built for it. Its
 * {@code computeIfAbsent} creates a missing counter exactly once, and {@link LongAdder} is a
 * counter designed for many threads adding at once.
 */
public final class SalesBoard {

    private final Map<String, LongAdder> sold = new ConcurrentHashMap<>();

    /** Records one unit of a product. Safe to call from any thread. */
    public void recordSale(String product) {
        sold.computeIfAbsent(product, key -> new LongAdder()).increment();
    }

    /** A sorted copy, taken after the threads are finished, so the output order is always the same. */
    public Map<String, Long> totals() {
        Map<String, Long> copy = new TreeMap<>();
        sold.forEach((product, adder) -> copy.put(product, adder.sum()));
        return copy;
    }
}

package com.codechanakya.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

/**
 * Three ways to add up the same readings on four threads. All three give the same total; they
 * differ in how much the threads get in each other's way.
 */
public final class SharedTotals {

    private SharedTotals() {
    }

    /** Every thread adds every reading to one shared {@link AtomicLong}. Correct, but all threads queue up on one number. */
    public static long withOneAtomic(int[] data) throws Exception {
        AtomicLong total = new AtomicLong();
        runOnFourThreads(data, value -> total.addAndGet(value));
        return total.get();
    }

    /** {@link LongAdder} keeps several internal counters and adds them up when you ask, so threads rarely collide. */
    public static long withALongAdder(int[] data) throws Exception {
        LongAdder total = new LongAdder();
        runOnFourThreads(data, total::add);
        return total.sum();
    }

    /** No sharing at all: each chunk returns its own total and the main thread adds them. Usually the best design. */
    public static long withPrivateTotals(int[] data) throws Exception {
        try (ParallelProcessor processor = new ParallelProcessor(4)) {
            return processor.process(data, 4).sum();
        }
    }

    private static void runOnFourThreads(int[] data, Consumer<Integer> action) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Future<?>> tickets = new ArrayList<>();
            for (Chunk chunk : Chunk.split(data.length, 4)) {
                tickets.add(pool.submit(() -> {
                    for (int i = chunk.from(); i < chunk.to(); i++) {
                        action.accept(data[i]);
                    }
                }));
            }
            for (Future<?> ticket : tickets) {
                ticket.get();
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}

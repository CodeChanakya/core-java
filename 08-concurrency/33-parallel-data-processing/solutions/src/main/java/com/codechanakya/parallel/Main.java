package com.codechanakya.parallel;

import java.util.Locale;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Parallel Data Processing.
 *
 * <p>One million readings, worked through by several threads. Every number printed here can be
 * checked by hand, and every one of them is the same on every run. The program never prints how
 * long anything took: timings change from machine to machine, and a program whose output changes
 * cannot be checked automatically. The exercises ask you to measure the speed yourself.
 */
public class Main {

    private static final int N = 1_000_000;

    public static void main(String[] args) throws Exception {
        int[] data = Readings.generate(N);

        theData(data);
        Stats baseline = oneThread(data);
        cuttingIntoChunks();
        futuresFromAPool(data, baseline);
        doesTheChunkCountMatter(data, baseline);
        parallelStreams(data, baseline);
        sharingATotal(data, baseline);
        waitingWithADeadline();
        whenOneChunkFails(data);
        exerciseAnswers(data, baseline);
    }

    private static void theData(int[] data) {
        System.out.println("--- The data ---");
        System.out.println("  " + data.length + " readings, the first five are " + Arrays.toString(Arrays.copyOf(data, 5)));
        System.out.println("  Every block of 1000 holds each value from 0 to 999 once, so the sum should be 1000 x 499500.");
    }

    private static Stats oneThread(int[] data) {
        System.out.println();
        System.out.println("--- One thread, the plain loop ---");
        Stats baseline = ParallelProcessor.sequential(data);
        System.out.println("  " + baseline.describe());
        return baseline;
    }

    private static void cuttingIntoChunks() {
        System.out.println();
        System.out.println("--- Cutting the work into chunks ---");
        System.out.println("  1,000,000 in 4 : " + Chunk.split(N, 4));
        System.out.println("  10 in 3        : " + Chunk.split(10, 3) + "   (the remainder goes to the first chunks)");
    }

    private static void futuresFromAPool(int[] data, Stats baseline) throws Exception {
        System.out.println();
        System.out.println("--- Four workers, four tickets ---");
        try (ParallelProcessor processor = new ParallelProcessor(4)) {
            List<Future<Stats>> tickets = processor.submit(data, 4);
            System.out.println("  Tickets handed out: " + tickets.size());
            Stats merged = Stats.EMPTY;
            for (int i = 0; i < tickets.size(); i++) {
                Stats part = tickets.get(i).get();
                System.out.println("  Chunk " + (i + 1) + ": " + part.describe());
                merged = merged.merge(part);
            }
            System.out.println("  Merged: " + merged.describe());
            System.out.println("  Same as the single thread: " + merged.equals(baseline));
        }
    }

    private static void doesTheChunkCountMatter(int[] data, Stats baseline) throws Exception {
        System.out.println();
        System.out.println("--- Does the number of chunks change the answer? ---");
        boolean allSame = true;
        try (ParallelProcessor processor = new ParallelProcessor(4)) {
            for (int chunks : new int[]{1, 2, 3, 7, 16}) {
                Stats result = processor.process(data, chunks);
                System.out.printf("  %2d chunks: sum %d%n", chunks, result.sum());
                allSame &= result.equals(baseline);
            }
        }
        System.out.println("  Every answer equals the baseline: " + allSame);
    }

    private static void parallelStreams(int[] data, Stats baseline) {
        System.out.println();
        System.out.println("--- Parallel streams do the chunking for you ---");
        long sum = Arrays.stream(data).parallel().asLongStream().sum();
        System.out.println("  parallel sum equals the baseline: " + (sum == baseline.sum()));

        Map<Integer, Long> perHundred = new TreeMap<>(
                Arrays.stream(data).parallel().boxed()
                        .collect(Collectors.groupingByConcurrent(v -> v / 100, Collectors.counting())));
        System.out.println("  Readings per band of 100: " + perHundred);
        System.out.println("  The result is the same however the threads were scheduled; only the speed changes.");
    }

    private static void sharingATotal(int[] data, Stats baseline) throws Exception {
        System.out.println();
        System.out.println("--- Three ways to share a total ---");
        System.out.println("  one AtomicLong  : " + SharedTotals.withOneAtomic(data));
        System.out.println("  one LongAdder   : " + SharedTotals.withALongAdder(data));
        System.out.println("  private totals  : " + SharedTotals.withPrivateTotals(data));
        System.out.println("  All equal the baseline: "
                + (SharedTotals.withOneAtomic(data) == baseline.sum()
                && SharedTotals.withALongAdder(data) == baseline.sum()
                && SharedTotals.withPrivateTotals(data) == baseline.sum()));
    }

    private static void waitingWithADeadline() throws InterruptedException {
        System.out.println();
        System.out.println("--- Waiting with a deadline ---");
        try (ParallelProcessor processor = new ParallelProcessor(1)) {
            Callable<String> slow = () -> {
                Thread.sleep(60_000);                           // would take a minute
                return "finished";
            };
            Future<String> ticket = processor.pool().submit(slow);
            try {
                ticket.get(100, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                System.out.println("  Gave up after 100 ms: " + e.getClass().getSimpleName());
                System.out.println("  cancel(true) returned " + ticket.cancel(true));
            } catch (ExecutionException e) {
                System.out.println("  Unexpected failure: " + e.getCause());
            }
        }
    }

    private static void whenOneChunkFails(int[] data) throws InterruptedException {
        System.out.println();
        System.out.println("--- One bad reading in the data ---");
        int[] damaged = data.clone();
        damaged[600_000] = -1;
        try (ParallelProcessor processor = new ParallelProcessor(4)) {
            List<Future<Stats>> tickets = processor.submit(damaged, 4);
            for (int i = 0; i < tickets.size(); i++) {
                try {
                    System.out.println("  Chunk " + (i + 1) + ": ok, " + tickets.get(i).get().count() + " readings");
                } catch (ExecutionException e) {
                    System.out.println("  Chunk " + (i + 1) + ": failed. " + e.getCause().getMessage());
                }
            }
        }
        System.out.println("  Only the chunk that held the bad reading failed.");
    }

    private static void exerciseAnswers(int[] data, Stats baseline) throws Exception {
        System.out.println();
        System.out.println("--- Exercise answers ---");
        System.out.println("  Above 950 is 49 per block of 1000 (B1): " + Arrays.stream(data).filter(v -> v > 950).count());
        System.out.println("  Smallest reading (B2): " + Arrays.stream(data).min().getAsInt());
        System.out.println("  Average (B3): " + String.format(Locale.ROOT, "%.1f", (double) baseline.sum() / baseline.count()));
        try (ParallelProcessor processor = new ParallelProcessor(4)) {
            System.out.println("  Histogram in 4 chunks (I1): " + Arrays.toString(processor.histogram(data, 4)));

            int[] damaged = data.clone();
            damaged[600_000] = -1;
            try {
                processor.processFailFast(damaged, 4);
            } catch (ExecutionException e) {
                System.out.println("  Fail fast (I2): " + e.getCause().getMessage());
            }
        }
        System.out.println("  Divide and conquer maximum (C1): " + ParallelProcessor.parallelMax(data));
    }
}

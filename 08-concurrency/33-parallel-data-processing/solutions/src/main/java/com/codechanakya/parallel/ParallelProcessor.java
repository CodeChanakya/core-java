package com.codechanakya.parallel;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Splits the data into chunks, gives each chunk to a worker, and merges the answers.
 *
 * <p>This is the "fork and join" idea in its plainest form. It is safe without a single lock
 * because of three habits:
 * <ol>
 *   <li>the data array is only <em>read</em> once the workers have it;</li>
 *   <li>every worker builds its own {@link Stats} and shares nothing while it works;</li>
 *   <li>the results are combined on one thread, after the workers hand them back.</li>
 * </ol>
 */
public final class ParallelProcessor implements AutoCloseable {

    private final ExecutorService pool;

    public ParallelProcessor(int threads) {
        this.pool = Executors.newFixedThreadPool(threads);
    }

    /** The plain loop on the calling thread. The answer everything else is compared with. */
    public static Stats sequential(int[] data) {
        return Stats.of(data, 0, data.length);
    }

    /** Hands out one task per chunk and returns the tickets without waiting. */
    public List<Future<Stats>> submit(int[] data, int chunks) {
        List<Future<Stats>> tickets = new ArrayList<>();
        for (Chunk chunk : Chunk.split(data.length, chunks)) {
            Callable<Stats> task = () -> Stats.of(data, chunk.from(), chunk.to());
            tickets.add(pool.submit(task));
        }
        return tickets;
    }

    /**
     * Processes the data in {@code chunks} pieces and returns the merged answer.
     *
     * @throws ExecutionException if any chunk failed; the cause says why
     */
    public Stats process(int[] data, int chunks) throws InterruptedException, ExecutionException {
        Stats total = Stats.EMPTY;
        for (Future<Stats> ticket : submit(data, chunks)) {
            total = total.merge(ticket.get());                    // waits for this chunk, then folds it in
        }
        return total;
    }

    /** The pool, for the demonstration of waiting with a deadline. */
    ExecutorService pool() {
        return pool;
    }

    @Override
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /** I1: counts readings per band of 100. Each chunk fills its own array; the arrays are added afterwards. */
    public long[] histogram(int[] data, int chunks) throws InterruptedException, ExecutionException {
        List<Future<long[]>> tickets = new ArrayList<>();
        for (Chunk chunk : Chunk.split(data.length, chunks)) {
            tickets.add(pool.submit(() -> {
                long[] bands = new long[10];
                for (int i = chunk.from(); i < chunk.to(); i++) {
                    bands[data[i] / 100]++;
                }
                return bands;
            }));
        }
        long[] total = new long[10];
        for (Future<long[]> ticket : tickets) {
            long[] part = ticket.get();
            for (int band = 0; band < total.length; band++) {
                total[band] += part[band];
            }
        }
        return total;
    }

    /** I2: stops at the first failure and cancels every ticket that is still going. */
    public Stats processFailFast(int[] data, int chunks) throws InterruptedException, ExecutionException {
        List<Future<Stats>> tickets = submit(data, chunks);
        Stats total = Stats.EMPTY;
        try {
            for (Future<Stats> ticket : tickets) {
                total = total.merge(ticket.get());
            }
        } catch (ExecutionException e) {
            for (Future<Stats> ticket : tickets) {
                ticket.cancel(true);                             // harmless for the ones already finished
            }
            throw e;
        }
        return total;
    }

    /** C1: divide and conquer. Split the range in half until it is small, then do it directly. */
    public static int parallelMax(int[] data) {
        return ForkJoinPool.commonPool().invoke(new MaxTask(data, 0, data.length));
    }

    private static final class MaxTask extends RecursiveTask<Integer> {
        private static final int SMALL_ENOUGH = 50_000;
        private final int[] data;
        private final int from;
        private final int to;

        MaxTask(int[] data, int from, int to) {
            this.data = data;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {
            if (to - from <= SMALL_ENOUGH) {
                int max = Integer.MIN_VALUE;
                for (int i = from; i < to; i++) {
                    max = Math.max(max, data[i]);
                }
                return max;
            }
            int middle = (from + to) / 2;
            MaxTask left = new MaxTask(data, from, middle);
            MaxTask right = new MaxTask(data, middle, to);
            left.fork();                                         // run the left half on another thread
            int rightMax = right.compute();                      // do the right half here
            return Math.max(left.join(), rightMax);
        }
    }
}

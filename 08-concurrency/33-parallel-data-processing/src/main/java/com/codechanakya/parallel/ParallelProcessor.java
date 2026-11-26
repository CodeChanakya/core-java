package com.codechanakya.parallel;

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
}

package com.codechanakya.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Builds reports on a small team of worker threads.
 *
 * <p>Creating a thread for every request is wasteful and, with enough requests, dangerous. An
 * {@link ExecutorService} keeps a fixed number of workers alive and hands each new task to
 * whichever worker is free. Tasks that arrive while every worker is busy wait in a queue.
 *
 * <p>The service is {@link AutoCloseable}, so {@code try-with-resources} always shuts the
 * workers down. Forgetting to do that is the most common executor mistake: the worker threads
 * are not daemons, so the program never ends.
 */
public final class ReportService implements AutoCloseable {

    private final ExecutorService pool;

    /** Names of the worker threads that have run at least one task. A concurrent set, because many threads add to it. */
    private final Set<String> workersUsed = ConcurrentHashMap.newKeySet();

    public ReportService(int workers) {
        this.pool = Executors.newFixedThreadPool(workers, new NamedThreads("report-worker"));
    }

    /**
     * Hands every request to the pool and returns straight away.
     *
     * <p>Each {@link Future} is a ticket for a result that may not exist yet. The caller keeps the
     * tickets and collects the reports later, in whatever order it likes.
     */
    public List<Future<Report>> submitAll(List<ReportRequest> requests) {
        List<Future<Report>> tickets = new ArrayList<>();
        for (ReportRequest request : requests) {
            tickets.add(pool.submit(() -> {                       // a Callable: it returns a Report
                workersUsed.add(Thread.currentThread().getName());
                return ReportBuilder.build(request);
            }));
        }
        return tickets;
    }

    /** Submits one task that returns nothing, for the cancellation demonstration. */
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    /** Which worker threads have done some work. */
    public Set<String> workersUsed() {
        return Set.copyOf(workersUsed);
    }

    /** True once {@link #close()} has started. */
    public boolean isShutdown() {
        return pool.isShutdown();
    }

    /**
     * Stops accepting new tasks, lets the running ones finish, and waits for the workers to exit.
     * If they take too long, they are interrupted.
     */
    @Override
    public void close() {
        pool.shutdown();                                          // no new tasks; finish what is queued
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();                               // out of patience: interrupt them
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();                   // keep the "I was interrupted" signal alive
        }
    }
}

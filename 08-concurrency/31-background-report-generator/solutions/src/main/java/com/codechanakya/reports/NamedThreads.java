package com.codechanakya.reports;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gives every worker thread a readable name such as {@code report-worker-1}.
 *
 * <p>Thread names are what you see in a stack trace, a log line or a debugger. A pool full of
 * threads called {@code pool-1-thread-3} is much harder to follow than one with a meaning.
 */
public final class NamedThreads implements ThreadFactory {

    private final String prefix;
    private final AtomicInteger next = new AtomicInteger(1);   // safe to bump from several threads

    public NamedThreads(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable task) {
        return new Thread(task, prefix + "-" + next.getAndIncrement());
    }
}

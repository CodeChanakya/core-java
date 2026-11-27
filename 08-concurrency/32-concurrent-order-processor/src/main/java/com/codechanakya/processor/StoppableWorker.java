package com.codechanakya.processor;

/**
 * A worker that keeps going until another thread tells it to stop.
 *
 * <p>The {@code volatile} keyword is the whole point. Without it, each thread is allowed to keep
 * its own copy of {@code running} and never look at memory again, so the worker may never see
 * the {@code false} that main wrote. With it, every read sees the most recent write.
 *
 * <p>{@code volatile} fixes <em>visibility</em>. It does not make {@code count++} safe, because
 * that is still read, add and write. That is why this class never shares the count.
 */
public final class StoppableWorker implements Runnable {

    private volatile boolean running = true;
    private long loops;                                   // only the worker thread touches this

    @Override
    public void run() {
        while (running) {
            loops++;
            Thread.onSpinWait();                          // a hint: we are only waiting for a flag
        }
    }

    public void stop() {
        running = false;
    }

    /** Only read this after {@code join()}; the join guarantees we see the final value. */
    public boolean didSomeWork() {
        return loops > 0;
    }
}

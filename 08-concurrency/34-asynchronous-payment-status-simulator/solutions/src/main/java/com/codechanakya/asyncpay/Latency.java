package com.codechanakya.asyncpay;

/**
 * Pretends that a remote service takes some time to answer.
 *
 * <p>Real services are slow because of networks and databases. Here we simply sleep. The delays
 * are chosen so far apart (tens of milliseconds for a normal answer, more than a second for a
 * slow one) that the outcome of every demonstration is the same on every run.
 */
final class Latency {

    private Latency() {
    }

    static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();                 // keep the signal for whoever is watching
            throw new IllegalStateException("Interrupted while waiting for a remote service", e);
        }
    }
}

package com.codechanakya.reports;

/**
 * The slow part. Building a report takes a little while, which is exactly why we want to do it
 * somewhere other than the thread that is talking to the user.
 *
 * <p>The method has no shared state: everything it needs comes in through the parameter and
 * everything it produces goes out through the return value. Methods like this are the easiest
 * kind to run on several threads, because there is nothing for two threads to fight over.
 */
public final class ReportBuilder {

    /** How long one report "waits for the database", in milliseconds. */
    private static final long SIMULATED_WAIT_MS = 40;

    private ReportBuilder() {
    }

    /**
     * Builds one report.
     *
     * @throws IllegalArgumentException if the request asks for no rows
     * @throws InterruptedException     if another thread cancels the work while it is waiting
     */
    public static Report build(ReportRequest request) throws InterruptedException {
        if (request.rows() <= 0) {
            throw new IllegalArgumentException("Report " + request.name() + " needs at least one row");
        }

        Thread.sleep(SIMULATED_WAIT_MS);          // stands in for a slow database or file

        long checksum = 17;
        for (int row = 1; row <= request.rows(); row++) {
            checksum = (checksum * 31 + row) % 1_000_003;
        }
        return new Report(request.name(), request.rows(), checksum);
    }
}

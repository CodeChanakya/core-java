package com.codechanakya.parallel;

/**
 * What we want to know about a stretch of readings.
 *
 * <p>The record is immutable and has a {@link #merge} method that joins two answers into one. That
 * pair of ideas is what makes parallel work easy: each thread builds its own {@code Stats} from
 * its own part of the data, with nothing shared and nothing to lock, and the main thread merges
 * the small results afterwards.
 *
 * @param count  how many readings were looked at
 * @param sum    their total
 * @param high   how many were above {@link #HIGH_LIMIT}
 * @param max    the largest one
 */
public record Stats(long count, long sum, long high, int max) {

    /** A reading above this counts as "high". */
    public static final int HIGH_LIMIT = 900;

    /** The answer for no readings at all. Merging it with anything changes nothing. */
    public static final Stats EMPTY = new Stats(0, 0, 0, Integer.MIN_VALUE);

    /**
     * Works through {@code data[from, to)}.
     *
     * @throws IllegalStateException if a reading is negative, which no sensor should produce
     */
    public static Stats of(int[] data, int from, int to) {
        long sum = 0;
        long high = 0;
        int max = Integer.MIN_VALUE;
        for (int i = from; i < to; i++) {
            int reading = data[i];
            if (reading < 0) {
                throw new IllegalStateException("Bad reading " + reading + " at position " + i);
            }
            sum += reading;
            if (reading > HIGH_LIMIT) {
                high++;
            }
            max = Math.max(max, reading);
        }
        return new Stats(to - from, sum, high, max);
    }

    /** Joins two answers. The order does not matter, which is why chunks may finish in any order. */
    public Stats merge(Stats other) {
        return new Stats(count + other.count, sum + other.sum, high + other.high, Math.max(max, other.max));
    }

    /** One line for the console. */
    public String describe() {
        return count + " readings, sum " + sum + ", " + high + " above " + HIGH_LIMIT + ", highest " + max;
    }
}

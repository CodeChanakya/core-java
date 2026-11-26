package com.codechanakya.parallel;

/**
 * A made-up stream of sensor readings, the same on every run.
 *
 * <p>Reading number {@code i} is {@code (i * 31 + 7) % 1000}. Because 31 and 1000 share no factor,
 * every block of 1000 readings contains each value from 0 to 999 exactly once. That makes every
 * answer in this program easy to check by hand: one million readings are 1000 complete blocks.
 */
public final class Readings {

    private Readings() {
    }

    /** The reading at position {@code i}. */
    public static int value(int i) {
        return (i * 31 + 7) % 1000;
    }

    /** The first {@code count} readings in an array. */
    public static int[] generate(int count) {
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = value(i);
        }
        return data;
    }
}

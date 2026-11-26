package com.codechanakya.processor;

/**
 * Units of one product that customers are trying to buy at the same moment.
 *
 * <p>Every implementation has the same promise: if the shelf holds 60 units, at most 60 units are
 * ever sold, however many threads call {@link #reserve(int)} at the same time.
 */
public interface Stock {

    /**
     * Takes {@code quantity} units off the shelf if enough are left.
     *
     * @return {@code true} if the units were reserved, {@code false} if there were not enough
     * @throws IllegalArgumentException if the quantity is zero or negative
     */
    boolean reserve(int quantity);

    /** Units still on the shelf. */
    int available();

    /** A short name, used in the report. */
    String name();
}

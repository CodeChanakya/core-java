package com.codechanakya.processor;

/**
 * The simplest fix: {@code synchronized}.
 *
 * <p>Every Java object has one built-in lock, called its monitor. A thread that enters a
 * {@code synchronized} method takes the monitor of {@code this}, and any other thread that wants
 * to enter a synchronized method of the same object has to wait outside until the first one
 * leaves. Only one thread at a time can be inside, so "check, then change" cannot be split in half.
 */
public final class SynchronizedStock implements Stock {

    private int units;

    public SynchronizedStock(int units) {
        this.units = units;
    }

    @Override
    public synchronized boolean reserve(int quantity) {
        requirePositive(quantity);
        if (units >= quantity) {          // check ...
            units -= quantity;            // ... then act. No other thread can slip in between.
            return true;
        }
        return false;
    }

    @Override
    public synchronized void release(int quantity) {
        requirePositive(quantity);
        units += quantity;
    }

    /** Reads need the lock too, otherwise a thread could see a stale number. */
    @Override
    public synchronized int available() {
        return units;
    }

    @Override
    public String name() {
        return "synchronized";
    }

    static void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive, got " + quantity);
        }
    }
}

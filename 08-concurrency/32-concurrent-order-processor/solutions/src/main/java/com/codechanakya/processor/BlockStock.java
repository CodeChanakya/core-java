package com.codechanakya.processor;

/**
 * B3: the same stock, with a synchronized <em>block</em> on a private lock object instead of a
 * synchronized method. Nobody outside the class can take the lock by accident, because it is not
 * the object itself.
 */
public final class BlockStock implements Stock {

    private final Object lock = new Object();
    private int units;

    public BlockStock(int units) {
        this.units = units;
    }

    @Override
    public boolean reserve(int quantity) {
        SynchronizedStock.requirePositive(quantity);
        synchronized (lock) {
            if (units >= quantity) {
                units -= quantity;
                return true;
            }
            return false;
        }
    }

    @Override
    public void release(int quantity) {
        SynchronizedStock.requirePositive(quantity);
        synchronized (lock) {
            units += quantity;
        }
    }

    @Override
    public int available() {
        synchronized (lock) {
            return units;
        }
    }

    @Override
    public String name() {
        return "sync block";
    }
}

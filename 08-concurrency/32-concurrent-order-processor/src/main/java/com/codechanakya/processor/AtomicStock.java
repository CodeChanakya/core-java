package com.codechanakya.processor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The same guarantee with no lock at all.
 *
 * <p>{@link AtomicInteger#compareAndSet} says: "set the value to X, but only if it is still Y".
 * The check and the change happen as one step inside the processor, so there is no gap. If another
 * thread got in first, the call returns {@code false} and we simply read the new value and try
 * again. Nobody ever waits for a lock, which is why this style is called lock-free.
 *
 * <p>It works well for one number. It does not scale to rules that involve several fields; for
 * those, use a lock.
 */
public final class AtomicStock implements Stock {

    private final AtomicInteger units;

    public AtomicStock(int units) {
        this.units = new AtomicInteger(units);
    }

    @Override
    public boolean reserve(int quantity) {
        SynchronizedStock.requirePositive(quantity);
        while (true) {
            int current = units.get();
            if (current < quantity) {
                return false;
            }
            if (units.compareAndSet(current, current - quantity)) {
                return true;              // we changed it from exactly what we read
            }
            // Someone else changed the number between our read and our write. Loop and look again.
        }
    }

    @Override
    public int available() {
        return units.get();
    }

    @Override
    public String name() {
        return "AtomicInteger";
    }
}

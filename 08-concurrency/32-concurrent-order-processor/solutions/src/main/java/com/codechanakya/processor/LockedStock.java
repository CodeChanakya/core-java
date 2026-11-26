package com.codechanakya.processor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The same guarantee with an explicit lock.
 *
 * <p>{@link ReentrantLock} does what {@code synchronized} does, and a little more: it can try for
 * the lock without waiting forever ({@code tryLock}), it can be interrupted while waiting, and
 * several locks can be held in an order you choose (Program 32's bank transfers use that).
 *
 * <p>The price is that you must unlock yourself. The {@code finally} block is not optional: if the
 * code in {@code try} throws and the lock is never released, every other thread waits forever.
 */
public final class LockedStock implements Stock {

    private final Lock lock = new ReentrantLock();
    private int units;

    public LockedStock(int units) {
        this.units = units;
    }

    @Override
    public boolean reserve(int quantity) {
        SynchronizedStock.requirePositive(quantity);
        lock.lock();
        try {
            if (units >= quantity) {
                units -= quantity;
                return true;
            }
            return false;
        } finally {
            lock.unlock();                // always runs, even when an exception is thrown above
        }
    }

    @Override
    public void release(int quantity) {
        SynchronizedStock.requirePositive(quantity);
        lock.lock();
        try {
            units += quantity;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int available() {
        lock.lock();
        try {
            return units;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String name() {
        return "ReentrantLock";
    }
}

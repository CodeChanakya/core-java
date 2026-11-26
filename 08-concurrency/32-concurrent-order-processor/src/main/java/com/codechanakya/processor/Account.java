package com.codechanakya.processor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bank account with its own lock. The id gives every account a fixed place in a line, which is
 * what {@link Bank} uses to avoid deadlock.
 */
public final class Account {

    private final int id;
    private long balance;
    private final Lock lock = new ReentrantLock();

    public Account(int id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public int id() {
        return id;
    }

    Lock lock() {
        return lock;
    }

    /** Callers must hold this account's lock. */
    long balance() {
        return balance;
    }

    /** Callers must hold this account's lock. */
    void change(long amount) {
        balance += amount;
    }

    /** A safe read, for printing after the threads have finished. */
    public long currentBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}

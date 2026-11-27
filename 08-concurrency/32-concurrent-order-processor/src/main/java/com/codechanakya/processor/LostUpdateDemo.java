package com.codechanakya.processor;

import java.util.concurrent.CountDownLatch;

/**
 * Shows a race condition on purpose, in a way that happens <em>every</em> time.
 *
 * <p>A real race depends on luck: the same program might lose an update today and not tomorrow,
 * which is what makes races so hard to find. To teach it, we take the luck away. A
 * {@link CountDownLatch} makes both threads read the balance first, and only then lets either of
 * them write. That is one of the orders the scheduler could pick on its own.
 *
 * <p>"balance = balance + 10" looks like one step, but the computer does three: read, add, write.
 */
public final class LostUpdateDemo {

    private LostUpdateDemo() {
    }

    /** Two threads each deposit 10 into a balance of 100. The right answer is 120. */
    public static int twoDepositsWithoutALock() throws InterruptedException {
        int[] balance = {100};
        CountDownLatch bothHaveRead = new CountDownLatch(2);

        Runnable deposit = () -> {
            int seen = balance[0];                    // 1. read
            bothHaveRead.countDown();
            try {
                bothHaveRead.await();                 // wait until the other thread has read too
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            balance[0] = seen + 10;                   // 2. add and 3. write: both write 110
        };

        Thread first = new Thread(deposit, "deposit-1");
        Thread second = new Thread(deposit, "deposit-2");
        first.start();
        second.start();
        first.join();
        second.join();
        return balance[0];
    }

    /** The same two deposits, with the read-add-write wrapped in one synchronized block. */
    public static int twoDepositsWithALock() throws InterruptedException {
        int[] balance = {100};
        Object lock = new Object();

        Runnable deposit = () -> {
            synchronized (lock) {                     // only one thread at a time in here
                int seen = balance[0];
                balance[0] = seen + 10;
            }
        };

        Thread first = new Thread(deposit, "deposit-1");
        Thread second = new Thread(deposit, "deposit-2");
        first.start();
        second.start();
        first.join();
        second.join();
        return balance[0];
    }
}

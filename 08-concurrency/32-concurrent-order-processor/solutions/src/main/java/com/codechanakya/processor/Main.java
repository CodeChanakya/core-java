package com.codechanakya.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Concurrent Order Processor.
 *
 * <p>Races are the one kind of bug you cannot show with an ordinary printout, because the wrong
 * answer appears only some of the time. So this program shows the <em>cause</em> with a forced
 * ordering, then shows that each fix gives the right answer. The unsafe version, which really
 * does race, is in the {@code broken} folder.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        aLostUpdate();
        guardingTheShelf();
        countingFromManyThreads();
        handingOverWork();
        aFlagOtherThreadsCanSee();
        transfersWithoutDeadlock();
        exerciseAnswers();
    }

    private static void aLostUpdate() throws InterruptedException {
        System.out.println("--- Two deposits of 10 into a balance of 100 ---");
        System.out.println("  Right answer             : 120");
        System.out.println("  Both read before writing : " + LostUpdateDemo.twoDepositsWithoutALock());
        System.out.println("  With a synchronized block: " + LostUpdateDemo.twoDepositsWithALock());
    }

    private static void guardingTheShelf() throws Exception {
        System.out.println();
        System.out.println("--- 100 customers, 8 threads, 60 units on the shelf ---");
        List<Stock> shelves = List.of(new SynchronizedStock(60), new LockedStock(60), new AtomicStock(60));
        for (Stock shelf : shelves) {
            OrderProcessor.Result result = OrderProcessor.process(shelf, 100, 8);
            System.out.printf("  %-14s accepted %d, refused %d, left on shelf %d%n",
                    result.stockName(), result.accepted(), result.rejected(), result.remaining());
        }
        System.out.println("  Never oversold, whichever way the threads were scheduled.");
    }

    private static void countingFromManyThreads() throws InterruptedException {
        System.out.println();
        System.out.println("--- Counting 300 sales from 6 threads ---");
        SalesBoard board = new SalesBoard();
        ExecutorService pool = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 300; i++) {
            int n = i;
            pool.execute(() -> board.recordSale(n % 6 < 3 ? "Pen" : (n % 6 < 5 ? "Notebook" : "Bag")));
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("  " + board.totals());
    }

    private static void handingOverWork() throws InterruptedException {
        System.out.println();
        System.out.println("--- 3 producers, 2 consumers, one queue of ten ---");
        OrderQueueDemo.Totals totals = OrderQueueDemo.run();
        System.out.println("  Processed " + totals.orders() + " orders worth " + totals.value() + " rupees");
    }

    private static void aFlagOtherThreadsCanSee() throws InterruptedException {
        System.out.println();
        System.out.println("--- Telling a worker to stop ---");
        StoppableWorker worker = new StoppableWorker();
        Thread thread = new Thread(worker, "stoppable-worker");
        thread.start();
        Thread.sleep(50);                         // let it run for a moment
        worker.stop();                            // writes the volatile flag
        thread.join();
        System.out.println("  Worker did some work: " + worker.didSomeWork());
        System.out.println("  Worker stopped: " + !thread.isAlive());
    }

    private static void transfersWithoutDeadlock() throws InterruptedException {
        System.out.println();
        System.out.println("--- Opposite transfers at the same time ---");
        Account a = new Account(1, 1000);
        Account b = new Account(2, 1000);

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                Bank.transfer(a, b, 1);           // A to B, one rupee at a time
            }
        }, "a-to-b"));
        threads.add(new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                Bank.transfer(b, a, 2);           // B to A, two rupees at a time
            }
        }, "b-to-a"));

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("  Account 1: " + a.currentBalance());
        System.out.println("  Account 2: " + b.currentBalance());
        System.out.println("  Total    : " + (a.currentBalance() + b.currentBalance()) + " (it was 2000 before)");
    }

    private static void exerciseAnswers() throws Exception {
        System.out.println();
        System.out.println("--- Exercise answers ---");
        OrderProcessor.Result all = OrderProcessor.process(new SynchronizedStock(100), 100, 8);
        System.out.println("  Stock 100, 100 customers (B1): accepted " + all.accepted() + ", refused " + all.rejected());
        OrderProcessor.Result crowd = OrderProcessor.process(new LockedStock(60), 200, 8);
        System.out.println("  Stock 60, 200 customers (B2): accepted " + crowd.accepted() + ", refused " + crowd.rejected());
        OrderProcessor.Result block = OrderProcessor.process(new BlockStock(60), 100, 8);
        System.out.println("  Synchronized block (B3): accepted " + block.accepted() + ", left " + block.remaining());

        Stock shelf = new AtomicStock(10);
        shelf.reserve(4);
        shelf.release(4);
        System.out.println("  Reserve 4 then release 4 (I1): " + shelf.available() + " on the shelf");

        try {
            Bank.transfer(new Account(1, 100), new Account(2, 100), 0);
        } catch (IllegalArgumentException e) {
            System.out.println("  Zero transfer (I2): " + e.getMessage());
        }

        Account a = new Account(1, 1000);
        Account b = new Account(2, 1000);
        Thread forward = new Thread(() -> moveUntilDone(a, b, 1, 300), "forward");
        Thread backward = new Thread(() -> moveUntilDone(b, a, 2, 300), "backward");
        forward.start();
        backward.start();
        forward.join();
        backward.join();
        System.out.println("  tryTransfer, no lock ordering (C1): total " + (a.currentBalance() + b.currentBalance()));
    }

    /** Keeps trying until each transfer succeeds. */
    private static void moveUntilDone(Account from, Account to, long amount, int times) {
        try {
            for (int i = 0; i < times; i++) {
                while (!Bank.tryTransfer(from, to, amount)) {
                    Thread.onSpinWait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

package com.codechanakya.reports;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Background Report Generator.
 *
 * <p>Everything printed here is the same on every run. Threads finish in an order nobody can
 * promise, so the program never prints from inside a worker (with one deliberate exception) and
 * always collects results in the order the tickets were handed out.
 */
public class Main {

    private static final List<ReportRequest> REQUESTS = List.of(
            new ReportRequest("Sales", 1200),
            new ReportRequest("Inventory", 800),
            new ReportRequest("Payroll", 450),
            new ReportRequest("Attendance", 2000),
            new ReportRequest("Tax", 300),
            new ReportRequest("Audit", 1500));

    public static void main(String[] args) throws Exception {
        aThreadByHand();
        oneAfterAnother();
        aPoolOfWorkers();
        whenATaskFails();
        stoppingATask();
        shuttingDown();
        exerciseAnswers();
    }

    private static void aThreadByHand() throws InterruptedException {
        System.out.println("--- A thread by hand ---");
        Thread worker = new Thread(
                () -> System.out.println("  Hello from thread " + Thread.currentThread().getName()),
                "report-worker");
        System.out.println("  Main is running on thread " + Thread.currentThread().getName());
        System.out.println("  Before start(), the worker is " + worker.getState());
        worker.start();                    // a NEW thread starts running the lambda
        worker.join();                     // main waits here until the worker is finished
        System.out.println("  After join(), the worker is " + worker.getState());
    }

    private static void oneAfterAnother() throws InterruptedException {
        System.out.println();
        System.out.println("--- One thread doing everything ---");
        for (ReportRequest request : REQUESTS) {
            System.out.println("  " + ReportBuilder.build(request).summary());
        }
        System.out.println("  Each report waited for the one before it.");
    }

    private static void aPoolOfWorkers() throws Exception {
        System.out.println();
        System.out.println("--- A pool of three workers ---");
        try (ReportService service = new ReportService(3)) {
            List<Future<Report>> tickets = service.submitAll(REQUESTS);
            System.out.println("  Handed over " + tickets.size() + " requests without waiting.");

            for (Future<Report> ticket : tickets) {
                System.out.println("  " + ticket.get().summary());          // get() waits if the report is not ready
            }
            int used = service.workersUsed().size();
            System.out.println("  Worker threads used: " + used + " (never more than 3: " + (used <= 3) + ")");
        }
    }

    private static void whenATaskFails() throws Exception {
        System.out.println();
        System.out.println("--- When one report fails ---");
        List<ReportRequest> mixed = List.of(
                new ReportRequest("Sales", 1200),
                new ReportRequest("Broken", 0),
                new ReportRequest("Tax", 300));

        try (ReportService service = new ReportService(2)) {
            List<Future<Report>> tickets = service.submitAll(mixed);
            for (int i = 0; i < tickets.size(); i++) {
                try {
                    System.out.println("  " + tickets.get(i).get().summary());
                } catch (ExecutionException e) {
                    // The exception happened on a worker thread. get() hands it to us wrapped in an
                    // ExecutionException; the real problem is the cause.
                    System.out.println("  " + mixed.get(i).name() + " failed: " + e.getCause().getMessage());
                }
            }
        }
        System.out.println("  The other two reports were not affected.");
    }

    private static void stoppingATask() throws Exception {
        System.out.println();
        System.out.println("--- Stopping a task that runs too long ---");
        CountDownLatch started = new CountDownLatch(1);
        AtomicBoolean sawInterrupt = new AtomicBoolean(false);
        boolean cancelled;

        try (ReportService service = new ReportService(1)) {
            Future<?> slow = service.submit(() -> {
                started.countDown();
                try {
                    Thread.sleep(60_000);                         // a report that would take a minute
                } catch (InterruptedException e) {
                    sawInterrupt.set(true);                       // cancel(true) interrupts the worker
                    Thread.currentThread().interrupt();
                }
            });
            started.await();                                      // wait until the worker has really begun
            cancelled = slow.cancel(true);
            System.out.println("  cancel(true) returned " + cancelled + ", isCancelled() is " + slow.isCancelled());
        }                                                         // close() waits for the worker to exit
        System.out.println("  The task noticed the interrupt: " + sawInterrupt.get());
    }

    private static void shuttingDown() {
        System.out.println();
        System.out.println("--- Shutting down properly ---");
        ReportService service = new ReportService(2);
        System.out.println("  Before close(), isShutdown is " + service.isShutdown());
        service.close();
        System.out.println("  After close(),  isShutdown is " + service.isShutdown());
        try {
            service.submitAll(new ArrayList<>(REQUESTS.subList(0, 1)));
        } catch (RejectedExecutionException e) {
            System.out.println("  A new request is refused: " + e.getClass().getSimpleName());
        }
    }

    private static void exerciseAnswers() throws Exception {
        System.out.println();
        System.out.println("--- Exercise answers ---");
        try (ReportService service = new ReportService(3)) {
            Future<Report> backup = service.submit(new ReportRequest("Backup", 600));            // B1, B2
            System.out.println("  Seventh report (B1, B2): " + backup.get().summary());

            Thread worker = new Thread(() -> { }, "report-worker");
            System.out.println("  Daemon? main: " + Thread.currentThread().isDaemon() + ", worker: " + worker.isDaemon());  // B3

            List<ReportRequest> mixed = List.of(new ReportRequest("Tax", 300), new ReportRequest("Broken", 0));
            System.out.println("  buildAll on two good ones (I1): "
                    + service.buildAll(List.of(new ReportRequest("Tax", 300), new ReportRequest("Audit", 1500))).size());
            System.out.println("  collectAll (I2): " + service.collectAll(mixed));

            System.out.println("  Quick job with a 1 s deadline (C1): "
                    + service.runWithDeadline(() -> "done", 1000));
            System.out.println("  Slow job with a 100 ms deadline (C1): "
                    + service.runWithDeadline(() -> {
                        Thread.sleep(60_000);
                        return "never";
                    }, 100));
        }
    }
}

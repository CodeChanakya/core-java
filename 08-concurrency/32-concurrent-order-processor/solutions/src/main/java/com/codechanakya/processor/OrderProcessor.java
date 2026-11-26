package com.codechanakya.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Sends a crowd of customers at one {@link Stock} at the same time.
 */
public final class OrderProcessor {

    /** What happened. A record: immutable, so it can be handed back across threads safely. */
    public record Result(String stockName, int accepted, int rejected, int remaining) {
    }

    private OrderProcessor() {
    }

    /**
     * Every customer wants one unit. {@code threads} workers share the customers between them.
     *
     * @return how many orders were accepted, how many refused, and what was left on the shelf
     */
    public static Result process(Stock stock, int customers, int threads) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Callable<Boolean>> orders = new ArrayList<>();
            for (int i = 0; i < customers; i++) {
                orders.add(() -> stock.reserve(1));
            }
            int accepted = 0;
            for (Future<Boolean> outcome : pool.invokeAll(orders)) {      // waits until every order is done
                if (outcome.get()) {
                    accepted++;
                }
            }
            return new Result(stock.name(), accepted, customers - accepted, stock.available());
        } finally {
            pool.shutdown();
        }
    }
}

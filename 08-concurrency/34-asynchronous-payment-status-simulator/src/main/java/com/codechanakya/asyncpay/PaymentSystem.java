package com.codechanakya.asyncpay;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wires the services together around one shared thread pool.
 *
 * <p>The pool is a cached pool: it makes a new thread when all are busy and reuses idle ones. That
 * suits tasks that mostly wait. The threads are daemons, so a slow gateway can never keep the
 * program from ending, and {@link #close()} interrupts whatever is still sleeping.
 */
public final class PaymentSystem implements AutoCloseable {

    private final ExecutorService executor;
    private final PaymentPipeline pipeline;
    private final GatewayService gateway;

    public PaymentSystem() {
        AtomicInteger next = new AtomicInteger(1);
        this.executor = Executors.newCachedThreadPool(task -> {
            Thread thread = new Thread(task, "pay-worker-" + next.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
        FraudService fraud = new FraudService(executor);
        BalanceService balances = new BalanceService(
                Map.of("ACC-A", new BigDecimal("100000"), "ACC-B", new BigDecimal("500")), executor);
        this.gateway = new GatewayService(executor);
        this.pipeline = new PaymentPipeline(executor, fraud, balances, gateway, new NotificationService(executor));
    }

    public PaymentPipeline pipeline() {
        return pipeline;
    }

    public GatewayService gateway() {
        return gateway;
    }

    public ExecutorService executor() {
        return executor;
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}

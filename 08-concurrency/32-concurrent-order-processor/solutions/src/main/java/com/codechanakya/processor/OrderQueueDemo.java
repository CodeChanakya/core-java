package com.codechanakya.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Producers put orders on a queue, consumers take them off.
 *
 * <p>A {@link BlockingQueue} does the hard waiting for us. {@code put} waits when the queue is full
 * and {@code take} waits when it is empty, so neither side needs {@code wait()} or
 * {@code notify()}. The queue holds only ten orders, so fast producers are slowed down to the
 * speed of the consumers instead of filling the memory.
 */
public final class OrderQueueDemo {

    /** What the consumers counted. */
    public record Totals(int orders, long value) {
    }

    private OrderQueueDemo() {
    }

    /** Three producers make 100 orders each. Two consumers process them. */
    public static Totals run() throws InterruptedException {
        BlockingQueue<OrderLine> queue = new ArrayBlockingQueue<>(10);
        AtomicInteger processed = new AtomicInteger();
        AtomicLong value = new AtomicLong();

        List<Thread> producers = new ArrayList<>();
        for (int p = 0; p < 3; p++) {
            int first = p * 100;
            producers.add(new Thread(() -> {
                try {
                    for (int id = first; id < first + 100; id++) {
                        queue.put(new OrderLine(id, id % 7 + 1));     // waits if the queue is full
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "producer-" + p));
        }

        List<Thread> consumers = new ArrayList<>();
        for (int c = 0; c < 2; c++) {
            consumers.add(new Thread(() -> {
                try {
                    while (true) {
                        OrderLine line = queue.take();                // waits if the queue is empty
                        if (line == OrderLine.POISON_PILL) {
                            return;                                   // time to go home
                        }
                        processed.incrementAndGet();
                        value.addAndGet(line.amount());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "consumer-" + c));
        }

        producers.forEach(Thread::start);
        consumers.forEach(Thread::start);
        for (Thread producer : producers) {
            producer.join();
        }
        for (int c = 0; c < consumers.size(); c++) {
            queue.put(OrderLine.POISON_PILL);                         // one pill for each consumer
        }
        for (Thread consumer : consumers) {
            consumer.join();
        }
        return new Totals(processed.get(), value.get());
    }
}

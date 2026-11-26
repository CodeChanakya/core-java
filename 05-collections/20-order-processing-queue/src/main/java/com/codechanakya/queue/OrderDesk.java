package com.codechanakya.queue;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Two ways of lining orders up, plus a history stack.
 *
 *  - fifo:     plain first-come-first-served.
 *  - urgent:   a PriorityQueue, express before standard, then by arrival.
 *  - history:  a Deque used as a stack, so the latest finished order is on top.
 */
public class OrderDesk {

    private final Queue<QueuedOrder> fifo = new ArrayDeque<>();
    private final Queue<QueuedOrder> urgent = new PriorityQueue<>(
            Comparator.comparing(QueuedOrder::getPriority)          // enum order: EXPRESS first
                    .thenComparingInt(QueuedOrder::getArrival));
    private final Deque<QueuedOrder> history = new ArrayDeque<>();

    /**
     * Every order goes into both lines, only so the demo can compare them side by side.
     * A real desk would pick one.
     */
    public void receive(QueuedOrder order) {
        fifo.offer(order);
        urgent.offer(order);
    }

    /** Next order in plain arrival order, or null when the line is empty. */
    public QueuedOrder nextInArrivalOrder() {
        return fifo.poll();
    }

    /** Next order by urgency, or null when the line is empty. Finished orders go on the history stack. */
    public QueuedOrder nextMostUrgent() {
        QueuedOrder order = urgent.poll();
        if (order != null) {
            history.push(order);
        }
        return order;
    }

    /** Looks at the head of the plain line without taking it. */
    public QueuedOrder peekArrivalOrder() {
        return fifo.peek();
    }

    /** The same call, but it throws NoSuchElementException on an empty line. */
    public QueuedOrder mustTakeArrivalOrder() {
        return fifo.remove();
    }

    /**
     * Takes back the most recently finished order and returns it to the urgent line,
     * where its priority decides its place.
     */
    public QueuedOrder undoLast() {
        QueuedOrder last = history.pollFirst();
        if (last != null) {
            urgent.offer(last);
        }
        return last;
    }

    public int waiting() {
        return urgent.size();
    }

    public int finished() {
        return history.size();
    }

    public QueuedOrder lastFinished() {
        return history.peekFirst();
    }
}

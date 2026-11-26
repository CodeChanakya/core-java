package com.codechanakya.queue;

/**
 * An order waiting to be processed.
 *
 * The arrival number is what makes the priority queue fair: two express orders
 * are handled in the order they came in, not in some arbitrary order.
 */
public class QueuedOrder {

    private static int arrivals = 0;

    private final String id;
    private final String customer;
    private final Priority priority;
    private final int arrival;

    public QueuedOrder(String id, String customer, Priority priority) {
        this.id = id;
        this.customer = customer;
        this.priority = priority;
        this.arrival = ++arrivals;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        return id + " (" + customer + ", " + priority.name().toLowerCase() + ")";
    }
}

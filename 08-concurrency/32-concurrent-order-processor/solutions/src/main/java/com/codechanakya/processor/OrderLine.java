package com.codechanakya.processor;

/**
 * One order waiting in the queue.
 *
 * @param id     the order number
 * @param amount the value in rupees
 */
public record OrderLine(int id, int amount) {

    /** A "stop" marker. A consumer that takes this one knows there is nothing more to come. */
    public static final OrderLine POISON_PILL = new OrderLine(-1, 0);
}

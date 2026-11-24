package com.codechanakya.orders;

import java.util.ArrayList;

/**
 * One customer order and the path it has taken through its statuses.
 *
 * Three keywords from this phase appear here:
 *   final   a value that is set once and never changes
 *   static  belongs to the class itself, not to any one order
 *   enum    the status is an OrderStatus, never free text
 */
public class Order {

    // A constant: static (one copy for the whole class) and final (never reassigned).
    public static final String ID_PREFIX = "ORD-";

    // static state is shared by every Order. Each new order takes the next number.
    private static int nextNumber = 7001;
    private static int createdCount = 0;

    private final String id;
    private final String customer;
    private OrderStatus status = OrderStatus.NEW;
    private final ArrayList<OrderStatus> path = new ArrayList<>();

    public Order(String customer) {
        this.id = ID_PREFIX + nextNumber;
        this.customer = customer;
        nextNumber++;
        createdCount++;
        path.add(status);
    }

    /** How many orders have been created since the program started. Called as Order.totalCreated(). */
    public static int totalCreated() {
        return createdCount;
    }

    /**
     * Moves the order to a new status if the rules allow it.
     * Returns false, and changes nothing, when the move is not allowed.
     */
    public boolean moveTo(OrderStatus next) {
        if (!status.canMoveTo(next)) {
            return false;
        }
        status = next;
        path.add(next);
        return true;
    }

    /** The journey so far, for example "New > Paid > Packed". */
    public String pathText() {
        StringBuilder text = new StringBuilder();
        for (OrderStatus step : path) {
            if (text.length() > 0) {
                text.append(" > ");
            }
            text.append(step.getLabel());
        }
        return text.toString();
    }

    /** B2: true once the order has reached a status it cannot leave. */
    public boolean isFinished() {
        return status.isFinished();
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }
}

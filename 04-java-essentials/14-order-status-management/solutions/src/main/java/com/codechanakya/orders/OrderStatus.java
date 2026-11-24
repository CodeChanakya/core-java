package com.codechanakya.orders;

import java.util.EnumSet;
import java.util.Set;

/**
 * Every state an order can be in, and the moves allowed between them.
 *
 * Program 13 kept the status as a String, so a typo like "PAYED" compiled and
 * silently never matched. An enum is a fixed list of named values: the
 * compiler knows them all, so a misspelling is an error on the line where you
 * wrote it, and a switch over an enum can be checked for missing cases.
 *
 * An enum is also a class. These constants carry a label and a flag, and the
 * type has methods like any other.
 */
public enum OrderStatus {

    NEW("New", false),
    PAID("Paid", false),
    PACKED("Packed", false),
    SHIPPED("Shipped", false),
    DELIVERED("Delivered", false),
    CANCELLED("Cancelled", true),
    RETURNED("Returned", false),   // B1: no longer final, a refund can follow
    REFUNDED("Refunded", true);

    private final String label;
    private final boolean finished;

    // Constructors of an enum are always private; you never call them yourself.
    // Java runs this once for each constant above, when the enum is first used.
    OrderStatus(String label, boolean finished) {
        this.label = label;
        this.finished = finished;
    }

    public String getLabel() {
        return label;
    }

    /** True for states an order can never leave. */
    public boolean isFinished() {
        return finished;
    }

    /**
     * The states this one may move to. The rules live here, next to the
     * values, so they cannot drift apart from them.
     *
     * The switch has no default branch on purpose: if someone adds a new
     * constant and forgets to handle it here, this stops compiling.
     * EnumSet is a set built for enum values (Phase 5 covers sets).
     */
    public Set<OrderStatus> nextStates() {
        return switch (this) {
            case NEW -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(PACKED, CANCELLED);
            case PACKED -> EnumSet.of(SHIPPED);
            case SHIPPED -> EnumSet.of(DELIVERED);
            case DELIVERED -> EnumSet.of(RETURNED);
            case RETURNED -> EnumSet.of(REFUNDED);
            case CANCELLED, REFUNDED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    /**
     * I2: turns text typed by a person into a status. Ignores case and spaces,
     * and returns null for anything unknown instead of throwing like valueOf.
     */
    public static OrderStatus fromText(String text) {
        String cleaned = text.trim().toUpperCase();
        for (OrderStatus status : values()) {
            if (status.name().equals(cleaned)) {
                return status;
            }
        }
        return null;
    }

    public boolean canMoveTo(OrderStatus next) {
        return nextStates().contains(next);
    }
}

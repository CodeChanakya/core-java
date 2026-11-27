package com.codechanakya.shop;

import java.util.EnumSet;
import java.util.Set;

/**
 * Where an order is in its life. The allowed moves are written down once, here, so no other class
 * has to remember them.
 */
public enum OrderStatus {
    NEW, PAID, SHIPPED, DELIVERED, CANCELLED;

    public Set<OrderStatus> next() {
        return switch (this) {
            case NEW -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED -> EnumSet.of(DELIVERED);
            case DELIVERED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    public boolean canMoveTo(OrderStatus target) {
        return next().contains(target);
    }
}

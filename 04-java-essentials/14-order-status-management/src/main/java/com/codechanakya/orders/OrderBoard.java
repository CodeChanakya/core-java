package com.codechanakya.orders;

import java.util.EnumMap;
import java.util.Map;

/**
 * Summary figures across many orders.
 *
 * The class is final (it cannot be extended) and its constructor is private
 * (it cannot be created), because it only holds static helper methods and
 * there is nothing to extend or instantiate. You call OrderBoard.countByStatus(...)
 * the way you call Math.max(...).
 */
public final class OrderBoard {

    private OrderBoard() {
    }

    /**
     * How many orders sit in each status, including statuses with none.
     * An EnumMap is a map keyed by an enum. It keeps the keys in the order
     * the constants were declared, so reports always read NEW first.
     */
    public static Map<OrderStatus, Integer> countByStatus(Order[] orders) {
        Map<OrderStatus, Integer> counts = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, 0);
        }
        for (Order order : orders) {
            counts.put(order.getStatus(), counts.get(order.getStatus()) + 1);
        }
        return counts;
    }

    public static void print(Map<OrderStatus, Integer> counts) {
        System.out.println("--- Orders by status ---");
        for (Map.Entry<OrderStatus, Integer> entry : counts.entrySet()) {
            System.out.println(String.format("%-10s %d", entry.getKey().getLabel(), entry.getValue()));
        }
    }
}

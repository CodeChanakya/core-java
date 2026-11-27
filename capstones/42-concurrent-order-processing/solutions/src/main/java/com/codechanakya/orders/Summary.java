package com.codechanakya.orders;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Exercise 3: how many orders ended in each status. Every status appears, even with a zero. */
public final class Summary {

    private Summary() {
    }

    public static Map<OrderStatus, Integer> countByStatus(List<OrderResult> results) {
        Map<OrderStatus, Integer> counts = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, 0);
        }
        results.forEach(r -> counts.merge(r.status(), 1, Integer::sum));
        return counts;
    }
}

package com.codechanakya.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * A placed order. {@code total} is what the customer pays, after discounts.
 */
public record Order(int id, int customerId, OrderStatus status, List<OrderLine> lines, BigDecimal discount,
                    BigDecimal total, LocalDate placedOn) {

    public Order {
        lines = List.copyOf(lines);
    }

    public BigDecimal subtotal() {
        return lines.stream().map(OrderLine::total).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    public Order withStatus(OrderStatus newStatus) {
        return new Order(id, customerId, newStatus, lines, discount, total, placedOn);
    }
}

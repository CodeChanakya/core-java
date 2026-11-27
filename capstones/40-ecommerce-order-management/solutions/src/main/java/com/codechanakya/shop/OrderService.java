package com.codechanakya.shop;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The shop's rules for an order's life. It prices an order, applies every {@link DiscountPolicy}, takes
 * payment through the {@link PaymentGateway} and moves the order along its allowed statuses.
 */
public class OrderService {

    private final ShopStore store;
    private final PaymentGateway gateway;
    private final List<DiscountPolicy> policies;
    private final Clock clock;

    public OrderService(ShopStore store, PaymentGateway gateway, List<DiscountPolicy> policies, Clock clock) {
        this.store = store;
        this.gateway = gateway;
        this.policies = List.copyOf(policies);
        this.clock = clock;
    }

    /**
     * Prices and places an order.
     *
     * @param quantities sku to quantity; sorted by sku so lines are always in the same order
     * @param coupon     a coupon code, or null
     */
    public Order place(int customerId, Map<String, Integer> quantities, String coupon) {
        store.findCustomer(customerId).orElseThrow(() -> new ShopException("No customer with id " + customerId));
        if (quantities.isEmpty()) {
            throw new ShopException("An order needs at least one item");
        }
        List<OrderLine> lines = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : new TreeMap<>(quantities).entrySet()) {
            if (entry.getValue() <= 0) {
                throw new ShopException("Quantity for " + entry.getKey() + " must be positive, but was " + entry.getValue());
            }
            Product product = store.findProduct(entry.getKey())
                    .orElseThrow(() -> new ShopException("No product " + entry.getKey()));
            lines.add(new OrderLine(product.sku(), entry.getValue(), product.price()));
        }
        BigDecimal subtotal = lines.stream().map(OrderLine::total).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
        BigDecimal discount = policies.stream()
                .map(policy -> policy.discount(lines, subtotal, coupon))
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add)
                .min(subtotal);
        return store.placeOrder(customerId, lines, discount, LocalDate.now(clock));
    }

    public void pay(int orderId, String card) {
        Order order = require(orderId);
        requireMove(order, OrderStatus.PAID);
        if (!gateway.charge(card, order.total())) {
            throw new ShopException("Payment of " + order.total().toPlainString() + " was declined");
        }
        store.updateStatus(orderId, OrderStatus.PAID);
    }

    public void ship(int orderId) {
        move(orderId, OrderStatus.SHIPPED);
    }

    public void deliver(int orderId) {
        move(orderId, OrderStatus.DELIVERED);
    }

    /** Only an unpaid or paid order can be cancelled. The stock goes back on the shelf. */
    public void cancel(int orderId) {
        Order order = require(orderId);
        requireMove(order, OrderStatus.CANCELLED);
        store.cancelAndRestock(orderId);
    }

    public Order order(int id) {
        return require(id);
    }

    private void move(int orderId, OrderStatus target) {
        Order order = require(orderId);
        requireMove(order, target);
        store.updateStatus(orderId, target);
    }

    private static void requireMove(Order order, OrderStatus target) {
        if (!order.status().canMoveTo(target)) {
            throw new ShopException("Order " + order.id() + " is " + order.status() + " and cannot become " + target);
        }
    }

    private Order require(int id) {
        return store.findOrder(id).orElseThrow(() -> new ShopException("No order with id " + id));
    }
}

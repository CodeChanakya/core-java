package com.codechanakya.shop;

import java.math.BigDecimal;
import java.util.ArrayList;

/** Someone who places orders. Loyalty members get a discount on every order. */
public class Customer {

    private static final BigDecimal LOYALTY_DISCOUNT = new BigDecimal("0.10");

    private final String id;
    private final String name;
    private final String email;
    private final boolean loyaltyMember;
    private final ArrayList<Order> orders = new ArrayList<>();   // C1

    public Customer(String id, String name, String email, boolean loyaltyMember) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.loyaltyMember = loyaltyMember;
    }

    /** The discount as a fraction of the subtotal: 0.05 for members, 0 for everyone else. */
    public BigDecimal discountRate() {
        return loyaltyMember ? LOYALTY_DISCOUNT : BigDecimal.ZERO;
    }

    /** C1: called by OrderService so a customer can see their own history. */
    void recordOrder(Order order) {
        orders.add(order);
    }

    public void printOrders() {
        System.out.println("Orders for " + name + ":");
        for (Order order : orders) {
            System.out.println("  " + order.getId() + "  " + order.getStatus() + "  " + order.total().toPlainString());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLoyaltyMember() {
        return loyaltyMember;
    }
}

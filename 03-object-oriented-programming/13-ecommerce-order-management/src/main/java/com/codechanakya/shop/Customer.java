package com.codechanakya.shop;

import java.math.BigDecimal;

/** Someone who places orders. Loyalty members get a discount on every order. */
public class Customer {

    private static final BigDecimal LOYALTY_DISCOUNT = new BigDecimal("0.05");

    private final String id;
    private final String name;
    private final String email;
    private final boolean loyaltyMember;

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

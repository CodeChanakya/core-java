package com.codechanakya.shop;

import java.math.BigDecimal;
import java.util.List;

/**
 * One way of giving money off. A policy looks at the lines and the coupon and says how much to take off.
 * The service adds up every policy, so a new offer is a new class, and nothing else changes.
 */
@FunctionalInterface
public interface DiscountPolicy {

    BigDecimal discount(List<OrderLine> lines, BigDecimal subtotal, String coupon);
}

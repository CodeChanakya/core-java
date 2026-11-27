package com.codechanakya.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** The shop's current offers. */
public final class Discounts {

    private Discounts() {
    }

    /** Ten percent off any line where ten or more of the same product are bought. */
    public static final DiscountPolicy BULK = (lines, subtotal, coupon) -> lines.stream()
            .filter(line -> line.quantity() >= 10)
            .map(line -> percent(line.total(), 10))
            .reduce(zero(), BigDecimal::add);

    /** Five percent off the whole order when it comes to 5,000 or more. */
    public static final DiscountPolicy BIG_ORDER = (lines, subtotal, coupon) ->
            subtotal.compareTo(new BigDecimal("5000")) >= 0 ? percent(subtotal, 5) : zero();

    /** WELCOME5 gives five percent. FLAT100 takes 100 off orders of 1,000 or more. Anything else gives nothing. */
    public static final DiscountPolicy COUPON = (lines, subtotal, coupon) -> {
        if ("WELCOME5".equals(coupon)) {
            return percent(subtotal, 5);
        }
        if ("FLAT100".equals(coupon) && subtotal.compareTo(new BigDecimal("1000")) >= 0) {
            return new BigDecimal("100.00");
        }
        return zero();
    };

    public static List<DiscountPolicy> standard() {
        return List.of(BULK, BIG_ORDER, COUPON);
    }

    private static BigDecimal percent(BigDecimal amount, int percent) {
        return amount.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(2);
    }
}

package com.codechanakya.pricing;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Turns a coupon code into a discount amount.
 *
 * <ul>
 *   <li>{@code WELCOME10}: ten percent off, but never more than 200.</li>
 *   <li>{@code FLAT50}: 50 off, only when the subtotal is 300 or more.</li>
 *   <li>A missing or blank code means no coupon, and gives no discount.</li>
 *   <li>Codes are not case sensitive. Any other code is an error.</li>
 * </ul>
 */
public class CouponService {

    static final BigDecimal WELCOME_CAP = Money.of("200");
    static final BigDecimal FLAT_MINIMUM = Money.of("300");
    static final BigDecimal FLAT_AMOUNT = Money.of("50");

    public BigDecimal discountFor(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) {
            return Money.of("0");
        }
        switch (code.trim().toUpperCase(Locale.ROOT)) {
            case "WELCOME10":
                return Money.round(subtotal.multiply(new BigDecimal("0.10")).min(WELCOME_CAP));
            case "FLAT50":
                return subtotal.compareTo(FLAT_MINIMUM) >= 0 ? FLAT_AMOUNT : Money.of("0");
            default:
                throw new IllegalArgumentException("Unknown coupon: " + code.trim());
        }
    }
}

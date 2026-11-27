package com.codechanakya.pricing;

import java.math.BigDecimal;

/**
 * Works out the delivery charge.
 *
 * <p>Standard delivery costs 40 plus 20 for every started kilogram, and is free when the
 * subtotal is 999 or more. Express delivery adds a flat 100 and is never free.
 */
public class ShippingCalculator {

    static final BigDecimal FREE_FROM = Money.of("999");
    static final BigDecimal BASE = Money.of("40");
    static final BigDecimal PER_KG = Money.of("20");
    static final BigDecimal EXPRESS_EXTRA = Money.of("100");
    static final int MAXIMUM_GRAMS = 20_000; // B1

    public BigDecimal cost(BigDecimal subtotal, int weightGrams, boolean express) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("Weight must be positive, but was " + weightGrams);
        }
        if (weightGrams > MAXIMUM_GRAMS) { // B1
            throw new IllegalArgumentException("Parcel is too heavy: " + weightGrams + " g (maximum " + MAXIMUM_GRAMS + ")");
        }
        int startedKilos = (weightGrams + 999) / 1000;
        BigDecimal standard = BASE.add(PER_KG.multiply(BigDecimal.valueOf(startedKilos)));
        if (express) {
            return Money.round(standard.add(EXPRESS_EXTRA));
        }
        return subtotal.compareTo(FREE_FROM) >= 0 ? Money.of("0") : Money.round(standard);
    }
}

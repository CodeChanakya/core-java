package com.codechanakya.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Small helpers so every amount in the program has exactly two decimals. */
final class Money {

    private Money() {
    }

    static BigDecimal of(String text) {
        return round(new BigDecimal(text));
    }

    static BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}

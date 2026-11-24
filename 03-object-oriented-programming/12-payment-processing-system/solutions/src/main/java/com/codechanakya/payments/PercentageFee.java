package com.codechanakya.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** A fee that is a percentage of the amount, rounded to the nearest paisa. */
public class PercentageFee implements FeePolicy {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final BigDecimal percent;

    public PercentageFee(BigDecimal percent) {
        this.percent = percent;
    }

    @Override
    public BigDecimal feeFor(BigDecimal amount) {
        return amount.multiply(percent).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public String describe() {
        return percent.stripTrailingZeros().toPlainString() + "% fee";
    }
}

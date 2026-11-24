package com.codechanakya.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** I2: payments under 1000 are free; from 1000 upwards the fee is 1 percent. */
public class TieredFee implements FeePolicy {

    private static final BigDecimal THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal RATE = new BigDecimal("0.01");

    @Override
    public BigDecimal feeFor(BigDecimal amount) {
        if (amount.compareTo(THRESHOLD) < 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return amount.multiply(RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String describe() {
        return "tiered fee";
    }
}

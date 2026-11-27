package com.codechanakya.pricing;

import java.math.BigDecimal;

/** How loyal a customer is, and the discount that earns. */
public enum CustomerTier {

    STANDARD("0.00"),
    SILVER("0.05"),
    GOLD("0.10");

    private final BigDecimal discountRate;

    CustomerTier(String discountRate) {
        this.discountRate = new BigDecimal(discountRate);
    }

    /** The discount as a fraction of the subtotal: 0.05 means five percent. */
    public BigDecimal discountRate() {
        return discountRate;
    }
}

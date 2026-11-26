package com.codechanakya.analytics;

import java.math.BigDecimal;

/** One order line. A record, because it is plain data that never changes. */
public record Sale(String orderId, String region, Category category, String salesperson,
                   int units, BigDecimal unitPrice) {

    /** Convenience constructor taking the price as text, so the data table below stays readable. */
    public Sale(String orderId, String region, Category category, String salesperson, int units, String unitPrice) {
        this(orderId, region, category, salesperson, units, new BigDecimal(unitPrice));
    }

    public BigDecimal revenue() {
        return unitPrice.multiply(BigDecimal.valueOf(units));
    }
}

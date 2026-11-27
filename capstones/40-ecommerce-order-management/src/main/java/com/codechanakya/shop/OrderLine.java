package com.codechanakya.shop;

import java.math.BigDecimal;

/** One product and how many of it were ordered, at the price on the day. */
public record OrderLine(String sku, int quantity, BigDecimal unitPrice) {

    public BigDecimal total() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

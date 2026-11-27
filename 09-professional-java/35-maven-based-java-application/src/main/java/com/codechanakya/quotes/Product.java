package com.codechanakya.quotes;

import java.math.BigDecimal;

/**
 * One item the shop sells.
 *
 * @param sku       the short code customers order by, such as {@code E-200}
 * @param name      the name shown on the quote
 * @param unitPrice the price of one unit, before tax
 * @param taxRate   the tax as a fraction, so 18 percent is {@code 0.18}
 */
public record Product(String sku, String name, BigDecimal unitPrice, BigDecimal taxRate) {

    public Product {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("A product needs a SKU.");
        }
        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + sku);
        }
        if (taxRate.signum() < 0 || taxRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 1: " + sku);
        }
    }
}

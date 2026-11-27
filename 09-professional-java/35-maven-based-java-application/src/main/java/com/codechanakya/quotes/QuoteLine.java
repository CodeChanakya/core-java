package com.codechanakya.quotes;

import java.math.BigDecimal;

/**
 * One priced row of a quote.
 *
 * @param product  the product
 * @param quantity how many
 * @param net      price after the quote's discount, before tax
 * @param tax      tax on the net amount
 */
public record QuoteLine(Product product, int quantity, BigDecimal net, BigDecimal tax) {

    public BigDecimal gross() {
        return net.add(tax);
    }
}

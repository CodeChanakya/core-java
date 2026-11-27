package com.codechanakya.quotes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Prices a list of items.
 *
 * <p>Rules: a subtotal of 1000 or more earns 10 percent off, 500 or more earns 5 percent off.
 * The discount is applied to every line first, and tax is worked out on the discounted amount,
 * because that is the amount the customer actually pays for.
 */
public class QuoteCalculator {

    static final BigDecimal TEN_PERCENT_FROM = new BigDecimal("1000");
    static final BigDecimal FIVE_PERCENT_FROM = new BigDecimal("500");
    static final BigDecimal FIFTEEN_PERCENT_FROM = new BigDecimal("3000"); // A2

    private final Catalog catalog;

    public QuoteCalculator(Catalog catalog) {
        this.catalog = catalog;
    }

    public Quote price(List<QuoteItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("A quote needs at least one item.");
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        for (QuoteItem item : items) {
            Product product = catalog.get(item.sku());
            subtotal = subtotal.add(product.unitPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }
        BigDecimal rate = discountRateFor(subtotal);

        List<QuoteLine> lines = new ArrayList<>();
        for (QuoteItem item : items) {
            Product product = catalog.get(item.sku());
            BigDecimal full = product.unitPrice().multiply(BigDecimal.valueOf(item.quantity()));
            BigDecimal net = money(full.multiply(BigDecimal.ONE.subtract(rate)));
            BigDecimal tax = money(net.multiply(product.taxRate()));
            lines.add(new QuoteLine(product, item.quantity(), net, tax));
        }
        return new Quote(lines, money(subtotal), rate, money(subtotal.multiply(rate)));
    }

    static BigDecimal discountRateFor(BigDecimal subtotal) {
        if (subtotal.compareTo(FIFTEEN_PERCENT_FROM) >= 0) { // A2
            return new BigDecimal("0.15");
        }
        if (subtotal.compareTo(TEN_PERCENT_FROM) >= 0) {
            return new BigDecimal("0.10");
        }
        if (subtotal.compareTo(FIVE_PERCENT_FROM) >= 0) {
            return new BigDecimal("0.05");
        }
        return BigDecimal.ZERO;
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}

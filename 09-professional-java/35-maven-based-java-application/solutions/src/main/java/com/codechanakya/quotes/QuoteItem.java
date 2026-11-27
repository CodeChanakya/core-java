package com.codechanakya.quotes;

/** What the customer asked for: a SKU and how many. */
public record QuoteItem(String sku, int quantity) {

    static final int MAX_QUANTITY = 100; // B2

    public QuoteItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1 for " + sku);
        }
        if (quantity > MAX_QUANTITY) { // B2
            throw new IllegalArgumentException("Quantity cannot be more than " + MAX_QUANTITY + " for " + sku);
        }
    }
}

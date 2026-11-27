package com.codechanakya.quotes;

/** What the customer asked for: a SKU and how many. */
public record QuoteItem(String sku, int quantity) {

    public QuoteItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1 for " + sku);
        }
    }
}

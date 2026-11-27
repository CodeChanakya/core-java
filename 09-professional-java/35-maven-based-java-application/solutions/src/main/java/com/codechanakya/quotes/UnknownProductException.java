package com.codechanakya.quotes;

/** Thrown when a quote asks for a SKU that is not in the catalog. */
public class UnknownProductException extends RuntimeException {

    public UnknownProductException(String sku) {
        super("Unknown product: " + sku);
    }
}

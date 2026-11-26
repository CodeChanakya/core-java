package com.codechanakya.inventory;

/** Thrown when a SKU is not in the inventory. Checked, because a mistyped SKU is a normal event. */
public class UnknownProductException extends Exception {

    public UnknownProductException(String sku) {
        super("No product with SKU " + sku);
    }
}

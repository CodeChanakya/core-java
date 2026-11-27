package com.codechanakya.shop;

public class OutOfStockException extends ShopException {

    public OutOfStockException(String sku, int wanted) {
        super("Not enough stock of " + sku + " for " + wanted);
    }
}

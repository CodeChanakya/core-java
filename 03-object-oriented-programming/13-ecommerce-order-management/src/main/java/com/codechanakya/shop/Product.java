package com.codechanakya.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Something the shop sells. Abstract, because every real product is either
 * physical or digital, and the two ship very differently.
 */
public abstract class Product {

    private final String sku;
    private final String name;
    private final BigDecimal price;
    private int stock;

    protected Product(String sku, String name, BigDecimal price, int stock) {
        this.sku = sku;
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.stock = stock;
    }

    /** Shipping charged for each unit of this product. */
    public abstract BigDecimal shippingPerUnit();

    public abstract String kind();

    public boolean hasStock(int quantity) {
        return quantity > 0 && quantity <= stock;
    }

    /** Takes units off the shelf. The caller must have checked hasStock first. */
    public void removeStock(int quantity) {
        if (!hasStock(quantity)) {
            throw new IllegalStateException("Not enough stock of " + name);
        }
        stock -= quantity;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}

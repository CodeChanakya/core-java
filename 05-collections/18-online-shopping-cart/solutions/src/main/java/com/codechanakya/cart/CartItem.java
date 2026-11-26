package com.codechanakya.cart;

import java.math.BigDecimal;

/**
 * One line in a shopping cart: a product, its unit price and how many were added.
 *
 * The cart merges repeat additions of the same SKU into one line, so quantity is
 * the only field that changes after construction.
 */
public class CartItem {

    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(String sku, String name, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1, got " + quantity);
        }
        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + unitPrice);
        }
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int more) {
        quantity += more;
    }

    /** Price times quantity, always shown with two decimals. */
    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2);
    }

    @Override
    public String toString() {
        return String.format("%-6s %-16s %2d x %8s = %9s", sku, name, quantity, unitPrice, lineTotal());
    }

    /** Replaces the quantity outright. Used when a shopper edits a line (I2). */
    public void setQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1, got " + newQuantity);
        }
        quantity = newQuantity;
    }
}

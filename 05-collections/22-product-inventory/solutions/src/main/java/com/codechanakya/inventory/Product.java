package com.codechanakya.inventory;

import java.math.BigDecimal;

/** A product on the shelf: what it is, what it costs, how many are in stock and when to reorder. */
public class Product {

    private final String sku;
    private final String name;
    private final Category category;
    private final BigDecimal price;
    private final int reorderLevel;
    private int stock;

    public Product(String sku, String name, Category category, String price, int stock, int reorderLevel) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = new BigDecimal(price);
        this.stock = stock;
        this.reorderLevel = reorderLevel;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    /** Package-private on purpose: only Inventory may change stock, so every change is recorded. */
    void changeStock(int delta) {
        stock += delta;
    }

    public boolean needsReorder() {
        return stock <= reorderLevel;
    }

    public BigDecimal stockValue() {
        return price.multiply(BigDecimal.valueOf(stock)).setScale(2);
    }

    @Override
    public String toString() {
        return String.format("%-6s %-16s %-11s %8s  stock %3d", sku, name, category, price, stock);
    }
}

package com.codechanakya.shop;

import java.math.BigDecimal;

/**
 * One line on an order: a product and how many of it.
 *
 * An OrderItem exists only as part of an order. If the order is thrown away,
 * its items go with it. That "part of" relationship is called composition.
 */
public class OrderItem {

    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal lineTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal shipping() {
        return product.shippingPerUnit().multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

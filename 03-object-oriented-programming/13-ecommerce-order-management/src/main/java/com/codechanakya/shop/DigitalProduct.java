package com.codechanakya.shop;

import java.math.BigDecimal;

/** A download. Nothing to post, so nothing to ship. */
public class DigitalProduct extends Product {

    public DigitalProduct(String sku, String name, BigDecimal price, int stock) {
        super(sku, name, price, stock);
    }

    @Override
    public BigDecimal shippingPerUnit() {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String kind() {
        return "Digital";
    }
}

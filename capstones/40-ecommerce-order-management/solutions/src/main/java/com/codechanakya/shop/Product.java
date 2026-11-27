package com.codechanakya.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Something the shop sells. */
public record Product(String sku, String name, Category category, BigDecimal price, int stock) {

    public Product {
        price = price.setScale(2, RoundingMode.HALF_UP);
    }
}

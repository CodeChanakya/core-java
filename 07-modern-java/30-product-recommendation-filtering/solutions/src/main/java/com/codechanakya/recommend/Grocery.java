package com.codechanakya.recommend;

import java.math.BigDecimal;

/** A food item. */
public record Grocery(String name, BigDecimal price, double rating, boolean vegetarian, int shelfLifeDays)
        implements Product {
}

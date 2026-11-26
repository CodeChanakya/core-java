package com.codechanakya.recommend;

import java.math.BigDecimal;

/** I2: a fourth kind of product. Adding it to "permits" is what makes every switch below demand a Toy case. */
public record Toy(String name, BigDecimal price, double rating, int minAge) implements Product {
}

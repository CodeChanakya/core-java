package com.codechanakya.recommend;

import java.math.BigDecimal;

/** An electronic gadget. */
public record Gadget(String name, BigDecimal price, double rating, int warrantyMonths, boolean wireless)
        implements Product {
}

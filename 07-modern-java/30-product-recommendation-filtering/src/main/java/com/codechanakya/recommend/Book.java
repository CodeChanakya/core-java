package com.codechanakya.recommend;

import java.math.BigDecimal;

/** A book. Records can implement an interface: the accessors name(), price() and rating() satisfy it. */
public record Book(String name, BigDecimal price, double rating, String author, int pages) implements Product {
}

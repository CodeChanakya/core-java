package com.codechanakya.repository;

import java.math.BigDecimal;

/**
 * A product, identified by a number.
 *
 * It also implements Comparable&lt;Product&gt;, which means "I know how to order
 * myself against another Product". Here the order is by price, cheapest first.
 */
public class Product implements Identifiable<Integer>, Comparable<Product> {

    private final int id;
    private final String name;
    private final BigDecimal price;

    public Product(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(Product other) {
        return price.compareTo(other.price);
    }

    @Override
    public String toString() {
        return id + " " + name + " " + price.toPlainString();
    }
}

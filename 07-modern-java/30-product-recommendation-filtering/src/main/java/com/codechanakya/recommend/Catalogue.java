package com.codechanakya.recommend;

import java.math.BigDecimal;
import java.util.List;

/** The shop's sample stock. */
public final class Catalogue {

    private Catalogue() {
    }

    public static List<Product> sample() {
        return List.of(
                new Book("The Pragmatic Programmer", new BigDecimal("1450.00"), 4.8, "Hunt and Thomas", 352),
                new Book("Clean Code", new BigDecimal("1200.00"), 4.4, "Robert Martin", 464),
                new Book("Java Concurrency in Practice", new BigDecimal("1900.00"), 4.7, "Brian Goetz", 544),
                new Book("A Pocket Guide to SQL", new BigDecimal("450.00"), 3.9, "Asha Menon", 120),
                new Gadget("Noise-Cancelling Headphones", new BigDecimal("1999.00"), 4.5, 24, true),
                new Gadget("USB-C Cable", new BigDecimal("299.00"), 4.2, 12, false),
                new Gadget("Mechanical Keyboard", new BigDecimal("3499.00"), 4.6, 24, false),
                new Grocery("Basmati Rice 5kg", new BigDecimal("620.00"), 4.3, true, 365),
                new Grocery("Fresh Paneer 200g", new BigDecimal("95.00"), 4.1, true, 3),
                new Grocery("Chicken Sausages", new BigDecimal("340.00"), 4.0, false, 5));
    }
}

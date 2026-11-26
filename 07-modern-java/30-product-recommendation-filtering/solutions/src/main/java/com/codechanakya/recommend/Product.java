package com.codechanakya.recommend;

import java.math.BigDecimal;

/**
 * Everything the shop sells is exactly one of three kinds.
 *
 * "sealed" with a "permits" list closes the family: no other class can implement Product.
 * That promise is what lets the compiler check, later, that a switch has covered every kind.
 */
public sealed interface Product permits Book, Gadget, Grocery, Toy {

    String name();

    BigDecimal price();

    double rating();
}

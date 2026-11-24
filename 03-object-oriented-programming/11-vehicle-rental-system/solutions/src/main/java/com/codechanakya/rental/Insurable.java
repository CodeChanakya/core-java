package com.codechanakya.rental;

import java.math.BigDecimal;

/**
 * A promise that a vehicle can be insured.
 *
 * An interface describes what something can do without saying how, and a class
 * may promise several of them. Cars and trucks are insurable; the motorbike is
 * not, and nothing forces it to pretend. That is why this is an interface and
 * not another parent class: Java lets a class extend only one parent.
 */
public interface Insurable {

    BigDecimal insurancePerDay();

    /** A default method: shared behaviour that implementing classes get for free. */
    default BigDecimal insuranceFor(int days) {
        return insurancePerDay().multiply(BigDecimal.valueOf(days));
    }
}

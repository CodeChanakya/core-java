package com.codechanakya.rental;

import java.math.BigDecimal;

/** I1: the cheapest thing on the lot. No insurance, no engine, no fuss. */
public class Bicycle extends Vehicle {

    private static final BigDecimal RATE = new BigDecimal("150.00");

    public Bicycle(String registration, String model) {
        super(registration, model);
    }

    @Override
    public BigDecimal dailyRate() {
        return RATE;
    }

    @Override
    public String kind() {
        return "Bicycle";
    }
}

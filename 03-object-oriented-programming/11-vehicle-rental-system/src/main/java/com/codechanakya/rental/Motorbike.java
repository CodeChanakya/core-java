package com.codechanakya.rental;

import java.math.BigDecimal;

/** A motorbike. The agency does not offer insurance on bikes, so it is not Insurable. */
public class Motorbike extends Vehicle {

    private static final BigDecimal RATE = new BigDecimal("800.00");

    private final int engineCc;

    public Motorbike(String registration, String model, int engineCc) {
        super(registration, model);
        this.engineCc = engineCc;
    }

    @Override
    public BigDecimal dailyRate() {
        return RATE;
    }

    @Override
    public String kind() {
        return "Bike";
    }

    public int getEngineCc() {
        return engineCc;
    }
}

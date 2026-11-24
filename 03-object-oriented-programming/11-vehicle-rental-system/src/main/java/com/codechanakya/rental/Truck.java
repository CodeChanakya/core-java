package com.codechanakya.rental;

import java.math.BigDecimal;

/** A truck. The daily rate grows with the load it can carry. */
public class Truck extends Vehicle implements Insurable {

    private static final BigDecimal BASE_RATE = new BigDecimal("3000.00");
    private static final BigDecimal PER_TONNE = new BigDecimal("500.00");
    private static final BigDecimal INSURANCE = new BigDecimal("700.00");

    private final int loadTonnes;

    public Truck(String registration, String model, int loadTonnes) {
        super(registration, model);
        this.loadTonnes = loadTonnes;
    }

    @Override
    public BigDecimal dailyRate() {
        return BASE_RATE.add(PER_TONNE.multiply(BigDecimal.valueOf(loadTonnes)));
    }

    @Override
    public String kind() {
        return "Truck";
    }

    @Override
    public BigDecimal insurancePerDay() {
        return INSURANCE;
    }

    public int getLoadTonnes() {
        return loadTonnes;
    }
}

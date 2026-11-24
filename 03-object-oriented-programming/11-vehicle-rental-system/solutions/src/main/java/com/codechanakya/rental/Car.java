package com.codechanakya.rental;

import java.math.BigDecimal;

/** A car. Seven-seaters cost a little more per day. */
public class Car extends Vehicle implements Insurable {

    private static final BigDecimal BASE_RATE = new BigDecimal("2500.00");
    private static final BigDecimal LARGE_CAR_EXTRA = new BigDecimal("500.00");
    private static final BigDecimal INSURANCE = new BigDecimal("300.00");

    private final int seats;

    public Car(String registration, String model, int seats) {
        super(registration, model);
        this.seats = seats;
    }

    @Override
    public BigDecimal dailyRate() {
        return seats > 5 ? BASE_RATE.add(LARGE_CAR_EXTRA) : BASE_RATE;
    }

    @Override
    public String kind() {
        return "Car";
    }

    @Override
    public BigDecimal insurancePerDay() {
        return INSURANCE;
    }

    public int getSeats() {
        return seats;
    }
}

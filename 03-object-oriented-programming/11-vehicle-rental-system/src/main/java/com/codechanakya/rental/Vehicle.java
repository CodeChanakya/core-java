package com.codechanakya.rental;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Anything the agency can rent out.
 *
 * The class is abstract because "a vehicle" on its own does not exist: you
 * rent a car, a bike or a truck. It still holds everything the types share
 * (registration, model, the long-rental discount) and leaves the parts that
 * differ, the daily rate and the kind, for each subclass to fill in.
 */
public abstract class Vehicle {

    /** Rentals of this many days or more get the long-rental discount. */
    private static final int LONG_RENTAL_DAYS = 7;
    private static final BigDecimal LONG_RENTAL_FACTOR = new BigDecimal("0.90");

    private final String registration;
    private final String model;

    protected Vehicle(String registration, String model) {
        this.registration = registration;
        this.model = model;
    }

    /** What this vehicle costs per day before any discount. Every subclass must supply it. */
    public abstract BigDecimal dailyRate();

    /** A one-word description such as "Car" or "Truck". */
    public abstract String kind();

    /**
     * The rent for a number of days. Written once here, so a long-rental
     * discount applies to every kind of vehicle in exactly the same way.
     */
    public BigDecimal rentalCost(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("A rental must be at least 1 day");
        }
        BigDecimal rent = dailyRate().multiply(BigDecimal.valueOf(days));
        if (days >= LONG_RENTAL_DAYS) {
            rent = rent.multiply(LONG_RENTAL_FACTOR);
        }
        return rent.setScale(2, RoundingMode.HALF_UP);
    }

    public String getRegistration() {
        return registration;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-8s %-26s %9s per day", registration, kind(), model, dailyRate().toPlainString());
    }
}

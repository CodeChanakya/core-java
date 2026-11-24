package com.codechanakya.rental;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * The agency's fleet and its pricing.
 *
 * The fleet is a list of Vehicle. The agency never needs to know whether an
 * entry is a car or a truck: it asks each vehicle for its own rate. The one
 * place it does ask "what are you?" is insurance, and it asks about an ability
 * (Insurable) rather than a class.
 */
public class RentalAgency {

    private final ArrayList<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    /** Rent plus insurance, when the vehicle can be insured. */
    public BigDecimal totalFor(Vehicle vehicle, int days) {
        BigDecimal total = vehicle.rentalCost(days);
        if (vehicle instanceof Insurable insurable) {
            total = total.add(insurable.insuranceFor(days));
        }
        return total;
    }

    /** One formatted quote line. */
    public String quote(Vehicle vehicle, int days) {
        String insurance = "-";
        if (vehicle instanceof Insurable insurable) {
            insurance = insurable.insuranceFor(days).toPlainString();
        }
        return String.format("%-8s %-26s %2d days  rent %9s  insurance %8s  total %10s",
                vehicle.kind(), vehicle.getModel(), days,
                vehicle.rentalCost(days).toPlainString(), insurance,
                totalFor(vehicle, days).toPlainString());
    }

    public void printFleet() {
        System.out.println("--- Fleet ---");
        for (Vehicle vehicle : fleet) {
            System.out.println(vehicle);
        }
    }

    public Vehicle find(String registration) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getRegistration().equals(registration)) {
                return vehicle;
            }
        }
        return null;
    }
}

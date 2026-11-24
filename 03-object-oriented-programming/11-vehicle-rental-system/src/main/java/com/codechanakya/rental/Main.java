package com.codechanakya.rental;

public class Main {

    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        agency.addVehicle(new Car("MH12AB1001", "Hyundai i20", 5));
        agency.addVehicle(new Car("MH12AB1002", "Toyota Innova", 7));
        agency.addVehicle(new Motorbike("MH12CD2001", "Royal Enfield Classic 350", 350));
        agency.addVehicle(new Truck("MH12EF3001", "Tata 407", 3));

        agency.printFleet();

        System.out.println();
        System.out.println("--- Quotes ---");
        System.out.println(agency.quote(agency.find("MH12AB1001"), 3));
        System.out.println(agency.quote(agency.find("MH12AB1002"), 7));
        System.out.println(agency.quote(agency.find("MH12CD2001"), 2));
        System.out.println(agency.quote(agency.find("MH12CD2001"), 10));
        System.out.println(agency.quote(agency.find("MH12EF3001"), 2));

        System.out.println();
        try {
            agency.quote(agency.find("MH12AB1001"), 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}

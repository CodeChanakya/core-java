package com.codechanakya.shop;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Catalog catalog = new Catalog();
        catalog.add(new PhysicalProduct("STAND-01", "Laptop Stand", new BigDecimal("1299.00"), 10, 2));
        catalog.add(new PhysicalProduct("KEYB-01", "Mechanical Keyboard", new BigDecimal("3499.00"), 3, 1));
        catalog.add(new DigitalProduct("BOOK-01", "Java Ebook", new BigDecimal("499.00"), 999));

        Customer asha = new Customer("C-1", "Asha Menon", "asha@example.com", true);
        Customer rahul = new Customer("C-2", "Rahul Verma", "rahul@example.com", false);
        OrderService service = new OrderService(catalog);

        catalog.printStock();

        System.out.println();
        Order first = service.placeOrder(asha, new String[] {"STAND-01", "BOOK-01"}, new int[] {1, 2});
        System.out.println(first.invoice());

        System.out.println();
        try {
            service.placeOrder(rahul, new String[] {"BOOK-01", "KEYB-01"}, new int[] {1, 5});
        } catch (IllegalStateException e) {
            System.out.println("Order refused: " + e.getMessage());
        }

        System.out.println();
        Order second = service.placeOrder(rahul, new String[] {"KEYB-01", "BOOK-01"}, new int[] {2, 1});
        System.out.println(second.invoice());

        System.out.println();
        System.out.println("Ship " + first.getId() + " before paying: " + outcome(first.markShipped()));
        System.out.println("Pay  " + first.getId() + ": " + outcome(first.markPaid()));
        System.out.println("Pay  " + first.getId() + " again: " + outcome(first.markPaid()));
        System.out.println("Ship " + first.getId() + ": " + outcome(first.markShipped()));
        System.out.println(first.getId() + " is now " + first.getStatus());

        System.out.println();
        catalog.printStock();
    }

    private static String outcome(boolean worked) {
        return worked ? "done" : "refused";
    }
}

package com.codechanakya.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // The same class, used for two different kinds of thing.
        Repository<Customer, String> customers = new InMemoryRepository<>();
        Repository<Product, Integer> products = new InMemoryRepository<>();

        customers.save(new Customer("C-101", "Asha Menon", "Pune"));
        customers.save(new Customer("C-102", "Rahul Verma", "Mumbai"));
        customers.save(new Customer("C-103", "Meera Iyer", "Chennai"));
        customers.save(new Customer("C-104", "Kiran Rao", "Delhi"));   // B1

        products.save(new Product(1, "Laptop Stand", new BigDecimal("1299.00")));
        products.save(new Product(2, "Mechanical Keyboard", new BigDecimal("3499.00")));
        products.save(new Product(3, "Wireless Mouse", new BigDecimal("899.00")));

        Util.printAll("Customers", customers.findAll());
        System.out.println();
        Util.printAll("Products", products.findAll());

        System.out.println();
        System.out.println("--- Finding and deleting ---");
        System.out.println("Customer C-102: " + customers.findById("C-102"));
        System.out.println("Customer C-999: " + customers.findById("C-999"));
        System.out.println("Product 3: " + products.findById(3));
        System.out.println("Delete product 2: " + products.delete(2));
        System.out.println("Delete product 2 again: " + products.delete(2));
        System.out.println("Products left: " + products.count());

        System.out.println();
        System.out.println("--- Saving with an existing id replaces ---");
        customers.save(new Customer("C-101", "Asha Menon", "Bengaluru"));
        System.out.println("C-101 is now: " + customers.findById("C-101"));
        System.out.println("Customers still: " + customers.count());

        System.out.println();
        System.out.println("--- Bounded generic methods ---");
        System.out.println("Most expensive product: " + Util.maxOf(products.findAll()));
        List<String> names = new ArrayList<>(List.of("Meera", "Asha", "Rahul"));
        System.out.println("Last name alphabetically: " + Util.maxOf(names));

        String[] queue = {"first", "second", "third"};
        Util.swap(queue, 0, 2);
        System.out.println("After swap: " + queue[0] + ", " + queue[1] + ", " + queue[2]);

        List<Integer> counts = List.of(4, 8, 15);
        List<Double> weights = List.of(1.5, 2.25);
        System.out.println("Sum of counts: " + Util.sum(counts));
        System.out.println("Sum of weights: " + Util.sum(weights));

        System.out.println("Cheapest product: " + Util.minOf(products.findAll()));
        System.out.println("Names sorted: " + Util.sorted(names) + ", original still " + names);
        System.out.println("Customer C-104 exists: " + customers.exists("C-104") + ", C-500 exists: " + customers.exists("C-500"));
        System.out.println("Customers: " + customers.count());   // B3

        Cache<String, Integer> cache = new Cache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        System.out.println("Cache after 3 puts into size 2: a=" + cache.get("a") + ", b=" + cache.get("b")
                + ", c=" + cache.get("c") + ", size " + cache.size());

        Pair<String, Integer> stock = new Pair<>("Laptop Stand", 12);
        System.out.println("A pair: " + stock + ", name length " + stock.getFirst().length()
                + ", doubled count " + (stock.getSecond() * 2));
    }
}

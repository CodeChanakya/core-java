package com.codechanakya.shop;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/** Runs a small online shop on an in-memory Derby database and prints what happens. */
public class Main {

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
        ConnectionProvider database = Schema.inMemory("ecommerce");
        try {
            Schema.create(database);
            ShopStore store = new JdbcShopStore(database);
            OrderService shop = new OrderService(store, new FakeGateway(), Discounts.standard(), clock);

            store.addProduct(new Product("BK-101", "Java Notebook", Category.BOOKS, new BigDecimal("450"), 40));
            store.addProduct(new Product("EL-201", "Wireless Mouse", Category.ELECTRONICS, new BigDecimal("899.50"), 15));
            store.addProduct(new Product("EL-202", "Mechanical Keyboard", Category.ELECTRONICS, new BigDecimal("4200"), 5));
            store.addProduct(new Product("HM-301", "Desk Lamp", Category.HOME, new BigDecimal("1250"), 12));
            store.addProduct(new Product("TY-401", "Puzzle Cube", Category.TOYS, new BigDecimal("199"), 60));
            Customer asha = store.addCustomer("Asha Rao", "Pune");
            Customer bilal = store.addCustomer("Bilal Khan", "Mumbai");
            Customer chitra = store.addCustomer("Chitra Nair", "Pune");

            System.out.println("== Catalogue");
            for (Product p : store.products()) {
                System.out.printf("  %-7s %-20s %-12s %9s  stock %2d%n", p.sku(), p.name(), p.category(), p.price().toPlainString(), p.stock());
            }

            System.out.println();
            System.out.println("== Placing orders");
            Order o1 = place(shop, "Asha: 12 notebooks, 2 lamps", asha, Map.of("BK-101", 12, "HM-301", 2), null);
            Order o2 = place(shop, "Bilal: 1 keyboard, 1 mouse, coupon WELCOME5", bilal, Map.of("EL-202", 1, "EL-201", 1), "WELCOME5");
            Order o3 = place(shop, "Chitra: 6 puzzle cubes, coupon FLAT100", chitra, Map.of("TY-401", 6), "FLAT100");
            place(shop, "Asha: 6 keyboards (only 4 left)", asha, Map.of("EL-202", 6), null);
            place(shop, "Bilal: 0 mice", bilal, Map.of("EL-201", 0), null);
            place(shop, "Nobody: 1 mouse", new Customer(99, "?", "?"), Map.of("EL-201", 1), null);
            place(shop, "Chitra: product ZZ-999", chitra, Map.of("ZZ-999", 1), null);
            Order o4 = place(shop, "Chitra: 4 keyboards", chitra, Map.of("EL-202", 4), null);
            show(store, "after the orders");

            System.out.println();
            System.out.println("== Order life");
            step("Pay order 1 with card 4111", () -> shop.pay(o1.id(), "4111"));
            step("Pay order 2 with card 0000", () -> shop.pay(o2.id(), "0000"));
            step("Ship order 2 before it is paid", () -> shop.ship(o2.id()));
            step("Pay order 2 with card 5500", () -> shop.pay(o2.id(), "5500"));
            step("Ship order 2", () -> shop.ship(o2.id()));
            step("Deliver order 2", () -> shop.deliver(o2.id()));
            step("Cancel order 2 (delivered)", () -> shop.cancel(o2.id()));
            step("Pay order 3 with card 4111", () -> shop.pay(o3.id(), "4111"));
            step("Cancel order 3 (paid, stock goes back)", () -> shop.cancel(o3.id()));
            step("Pay order 4 with card 4111", () -> shop.pay(o4.id(), "4111"));
            step("Ship order 1", () -> shop.ship(o1.id()));
            show(store, "after the order life");
            System.out.println("  -- stock after the order life (order 3 was cancelled, so its cubes are back)");
            for (Product p : store.products()) {
                System.out.printf("  %-7s %-20s stock %2d%n", p.sku(), p.name(), p.stock());
            }

            System.out.println();
            System.out.println("== Reports");
            List<Order> orders = store.orders();
            List<Product> catalogue = store.products();
            System.out.println("  Orders by status: " + Reports.countByStatus(orders));
            System.out.println("  Revenue by category: " + Reports.revenueByCategory(orders, catalogue));
            System.out.println("  Best sellers: " + Reports.bestSellers(orders, 3));
            System.out.println("  Top customers: " + Reports.topCustomers(orders, 2));
            System.out.println("  Average order value: " + Reports.averageOrderValue(orders).toPlainString());
            System.out.println("  Low stock (under 10): " + Reports.lowStock(catalogue, 10));
            System.out.println("  Customers by city: " + Reports.customersByCity(List.of(asha, bilal, chitra)));
        } finally {
            Schema.drop("ecommerce");
        }
    }

    private static Order place(OrderService shop, String label, Customer customer, Map<String, Integer> items, String coupon) {
        try {
            Order order = shop.place(customer.id(), items, coupon);
            System.out.printf("  %s: order %d, subtotal %s, discount %s, total %s%n", label, order.id(),
                    order.subtotal().toPlainString(), order.discount().toPlainString(), order.total().toPlainString());
            return order;
        } catch (ShopException e) {
            System.out.println("  " + label + ": refused, " + e.getMessage());
            return null;
        }
    }

    private static void step(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  " + label + ": done");
        } catch (ShopException e) {
            System.out.println("  " + label + ": refused, " + e.getMessage());
        }
    }

    private static void show(ShopStore store, String when) {
        System.out.println("  -- orders " + when);
        for (Order o : store.orders()) {
            System.out.printf("  #%d  customer %d  %-9s total %9s  lines %s%n", o.id(), o.customerId(), o.status(),
                    o.total().toPlainString(), o.lines().stream().map(l -> l.sku() + "x" + l.quantity()).toList());
        }
    }
}

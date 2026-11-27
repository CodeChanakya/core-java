package com.codechanakya.shop;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Reports on a list of orders, written as streams. Each method is a pure function of its input: give it
 * the same orders and it gives the same answer, which is what makes it easy to test.
 */
public final class Reports {

    private Reports() {
    }

    /** Orders that count as sales: everything except cancelled ones. */
    public static List<Order> sales(List<Order> orders) {
        return orders.stream().filter(o -> o.status() != OrderStatus.CANCELLED).toList();
    }

    /** How many orders are in each status. Every status appears, even with zero. */
    public static Map<OrderStatus, Long> countByStatus(List<Order> orders) {
        Map<OrderStatus, Long> counts = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, 0L);
        }
        orders.forEach(o -> counts.merge(o.status(), 1L, Long::sum));
        return counts;
    }

    /** Revenue per category, from line totals of sales only. Categories come out in the order they are declared in {@link Category}. */
    public static Map<Category, BigDecimal> revenueByCategory(List<Order> orders, List<Product> catalogue) {
        Map<String, Category> categoryOf = catalogue.stream().collect(Collectors.toMap(Product::sku, Product::category));
        return sales(orders).stream()
                .flatMap(o -> o.lines().stream())
                .collect(Collectors.groupingBy(line -> categoryOf.get(line.sku()),
                        () -> new TreeMap<>(),
                        Collectors.reducing(BigDecimal.ZERO.setScale(2), OrderLine::total, BigDecimal::add)));
    }

    /** Units sold per product, best seller first; ties go to the lower sku. */
    public static List<Map.Entry<String, Integer>> bestSellers(List<Order> orders, int limit) {
        return sales(orders).stream()
                .flatMap(o -> o.lines().stream())
                .collect(Collectors.groupingBy(OrderLine::sku, TreeMap::new, Collectors.summingInt(OrderLine::quantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .toList();
    }

    /** What each customer has spent on sales, biggest spender first; ties go to the lower id. */
    public static List<Map.Entry<Integer, BigDecimal>> topCustomers(List<Order> orders, int limit) {
        return sales(orders).stream()
                .collect(Collectors.groupingBy(Order::customerId, TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO.setScale(2), Order::total, BigDecimal::add)))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .toList();
    }

    /** The mean order value over sales, or zero when there are none. */
    public static BigDecimal averageOrderValue(List<Order> orders) {
        List<Order> sold = sales(orders);
        if (sold.isEmpty()) {
            return BigDecimal.ZERO.setScale(2);
        }
        BigDecimal sum = sold.stream().map(Order::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(sold.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    /** Products with fewer than {@code threshold} units left, as sku to stock, sorted by sku. */
    public static Map<String, Integer> lowStock(List<Product> catalogue, int threshold) {
        return catalogue.stream().filter(p -> p.stock() < threshold)
                .collect(Collectors.toMap(Product::sku, Product::stock, (a, b) -> a, TreeMap::new));
    }

    /** The names of customers by city, sorted. */
    public static Map<String, List<String>> customersByCity(List<Customer> customers) {
        return customers.stream().collect(Collectors.groupingBy(Customer::city, TreeMap::new,
                Collectors.mapping(Customer::name, Collectors.collectingAndThen(Collectors.toList(),
                        names -> names.stream().sorted().toList()))));
    }

}

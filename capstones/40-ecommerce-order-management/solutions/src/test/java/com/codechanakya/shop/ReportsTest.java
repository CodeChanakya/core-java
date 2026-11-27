package com.codechanakya.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** The reports are pure functions, so these tests need no database at all. */
class ReportsTest {

    private static final List<Product> CATALOGUE = List.of(
            new Product("BK-1", "Book", Category.BOOKS, new BigDecimal("100"), 3),
            new Product("EL-1", "Mouse", Category.ELECTRONICS, new BigDecimal("1000"), 50),
            new Product("TY-1", "Cube", Category.TOYS, new BigDecimal("50"), 9));

    private static Order order(int id, int customer, OrderStatus status, OrderLine... lines) {
        BigDecimal total = List.of(lines).stream().map(OrderLine::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(id, customer, status, List.of(lines), BigDecimal.ZERO, total, LocalDate.of(2025, 6, 30));
    }

    private static OrderLine line(String sku, int quantity, String price) {
        return new OrderLine(sku, quantity, new BigDecimal(price));
    }

    private final List<Order> orders = List.of(
            order(1, 1, OrderStatus.DELIVERED, line("BK-1", 2, "100.00"), line("EL-1", 1, "1000.00")),
            order(2, 2, OrderStatus.PAID, line("BK-1", 1, "100.00")),
            order(3, 1, OrderStatus.CANCELLED, line("EL-1", 9, "1000.00")),
            order(4, 2, OrderStatus.NEW, line("TY-1", 10, "50.00")));

    @Test
    @DisplayName("cancelled orders are not sales")
    void salesExcludeCancelled() {
        assertEquals(List.of(1, 2, 4), Reports.sales(orders).stream().map(Order::id).toList());
    }

    @Test
    @DisplayName("every status is counted, even those with no orders")
    void countByStatus() {
        Map<OrderStatus, Long> counts = Reports.countByStatus(orders);
        assertEquals(5, counts.size());
        assertEquals(1L, counts.get(OrderStatus.CANCELLED));
        assertEquals(0L, counts.get(OrderStatus.SHIPPED));
    }

    @Test
    void revenueByCategory() {
        Map<Category, BigDecimal> revenue = Reports.revenueByCategory(orders, CATALOGUE);
        assertEquals(new BigDecimal("300.00"), revenue.get(Category.BOOKS));
        assertEquals(new BigDecimal("1000.00"), revenue.get(Category.ELECTRONICS));
        assertEquals(new BigDecimal("500.00"), revenue.get(Category.TOYS));
    }

    @Test
    @DisplayName("best sellers: most units first, ties by sku, limit respected")
    void bestSellers() {
        assertEquals("[TY-1=10, BK-1=3]", Reports.bestSellers(orders, 2).toString());
        assertEquals(3, Reports.bestSellers(orders, 10).size());
    }

    @Test
    @DisplayName("top customers: biggest spender first, cancelled orders ignored")
    void topCustomers() {
        assertEquals("[1=1200.00, 2=600.00]", Reports.topCustomers(orders, 5).toString());
    }

    @Test
    void averageOrderValue() {
        assertEquals(new BigDecimal("600.00"), Reports.averageOrderValue(orders));
        assertEquals(new BigDecimal("0.00"), Reports.averageOrderValue(List.of()));
    }

    @Test
    void lowStock() {
        assertEquals("{BK-1=3, TY-1=9}", Reports.lowStock(CATALOGUE, 10).toString());
    }

    @Test
    void customersByCityAreSorted() {
        List<Customer> customers = List.of(new Customer(1, "Zed", "Pune"), new Customer(2, "Amy", "Pune"), new Customer(3, "Bo", "Goa"));
        assertEquals("{Goa=[Bo], Pune=[Amy, Zed]}", Reports.customersByCity(customers).toString());
    }

    @Test
    @DisplayName("exercise 2: sales per day skips cancelled orders and sorts by date")
    void salesPerDay() {
        List<Order> more = new java.util.ArrayList<>(orders);
        more.add(new Order(5, 1, OrderStatus.PAID, List.of(line("BK-1", 1, "100.00")), BigDecimal.ZERO,
                new BigDecimal("100.00"), LocalDate.of(2025, 6, 29)));
        assertEquals("{2025-06-29=1, 2025-06-30=3}", Reports.salesPerDay(more).toString());
    }

    @Test
    @DisplayName("exercise 3: repeat customers have two or more sales")
    void repeatCustomers() {
        // customer 1 has one sale and one cancelled order; customer 2 has two sales
        assertEquals(List.of(2), Reports.repeatCustomers(orders));
    }
}

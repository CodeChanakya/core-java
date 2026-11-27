package com.codechanakya.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcShopStoreTest {

    private ShopFixture shop;

    @BeforeEach
    void setUp() {
        shop = new ShopFixture();
    }

    @AfterEach
    void tearDown() {
        shop.close();
    }

    private static OrderLine line(String sku, int quantity, String price) {
        return new OrderLine(sku, quantity, new BigDecimal(price));
    }

    @Test
    @DisplayName("a saved order can be read back with its lines in sku order")
    void roundTrip() {
        Order placed = shop.store.placeOrder(shop.asha.id(),
                List.of(line("BK-1", 2, "100.00"), line("TY-1", 3, "50.00")), new BigDecimal("10.00"), LocalDate.of(2025, 6, 30));
        Order read = shop.store.findOrder(placed.id()).orElseThrow();
        assertEquals(OrderStatus.NEW, read.status());
        assertEquals(new BigDecimal("340.00"), read.total());
        assertEquals(List.of("BK-1", "TY-1"), read.lines().stream().map(OrderLine::sku).toList());
        assertEquals(LocalDate.of(2025, 6, 30), read.placedOn());
    }

    @Test
    @DisplayName("placing an order takes the stock")
    void takesStock() {
        shop.store.placeOrder(shop.asha.id(), List.of(line("EL-1", 2, "1000.00")), BigDecimal.ZERO, LocalDate.of(2025, 6, 30));
        assertEquals(1, shop.stock("EL-1"));
    }

    @Test
    @DisplayName("when the second line is out of stock, the first line's stock is given back and no order exists")
    void rollsBackEverything() {
        assertThrows(OutOfStockException.class, () -> shop.store.placeOrder(shop.asha.id(),
                List.of(line("BK-1", 5, "100.00"), line("EL-1", 4, "1000.00")), BigDecimal.ZERO, LocalDate.of(2025, 6, 30)));
        assertEquals(20, shop.stock("BK-1"));
        assertEquals(3, shop.stock("EL-1"));
        assertTrue(shop.store.orders().isEmpty());
    }

    @Test
    @DisplayName("cancelAndRestock marks the order cancelled and puts every line back")
    void cancelRestocks() {
        Order o = shop.store.placeOrder(shop.asha.id(), List.of(line("BK-1", 5, "100.00"), line("EL-1", 2, "1000.00")),
                BigDecimal.ZERO, LocalDate.of(2025, 6, 30));
        shop.store.cancelAndRestock(o.id());
        assertEquals(OrderStatus.CANCELLED, shop.store.findOrder(o.id()).orElseThrow().status());
        assertEquals(20, shop.stock("BK-1"));
        assertEquals(3, shop.stock("EL-1"));
    }

    @Test
    @DisplayName("the database refuses negative stock even from raw SQL")
    void checkRule() {
        Product broken = new Product("XX-1", "Bad", Category.HOME, BigDecimal.ONE, -1);
        ShopException e = assertThrows(ShopException.class, () -> shop.store.addProduct(broken));
        assertTrue(e.getMessage().contains("23513"));
    }

    @Test
    @DisplayName("adding the same sku twice is reported as a duplicate")
    void duplicateSku() {
        ShopException e = assertThrows(ShopException.class, () -> shop.store.addProduct(
                new Product("BK-1", "Again", Category.BOOKS, BigDecimal.ONE, 1)));
        assertEquals("Adding a product failed: that already exists", e.getMessage());
    }

    @Test
    @DisplayName("find methods return empty for unknown ids")
    void unknowns() {
        assertTrue(shop.store.findOrder(99).isEmpty());
        assertTrue(shop.store.findCustomer(99).isEmpty());
        assertTrue(shop.store.findProduct("nope").isEmpty());
    }

    @Test
    @DisplayName("a product name with SQL in it is stored as plain text")
    void injection() {
        shop.store.addProduct(new Product("IN-1", "x'); DROP TABLE product; --", Category.HOME, BigDecimal.ONE, 1));
        assertEquals("x'); DROP TABLE product; --", shop.store.findProduct("IN-1").orElseThrow().name());
        assertEquals(4, shop.store.products().size());
    }
}

package com.codechanakya.shop;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

/** A fresh in-memory shop for one test: three products, two customers, and a fixed clock. */
final class ShopFixture {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    final String name = "shop-test-" + COUNTER.incrementAndGet();
    final ConnectionProvider provider = Schema.inMemory(name);
    final ShopStore store = new JdbcShopStore(provider);
    final Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
    final Customer asha;
    final Customer bilal;

    ShopFixture() {
        Schema.create(provider);
        store.addProduct(new Product("BK-1", "Book", Category.BOOKS, new BigDecimal("100"), 20));
        store.addProduct(new Product("EL-1", "Mouse", Category.ELECTRONICS, new BigDecimal("1000"), 3));
        store.addProduct(new Product("TY-1", "Cube", Category.TOYS, new BigDecimal("50"), 100));
        asha = store.addCustomer("Asha", "Pune");
        bilal = store.addCustomer("Bilal", "Mumbai");
    }

    OrderService service(PaymentGateway gateway) {
        return new OrderService(store, gateway, Discounts.standard(), clock);
    }

    int stock(String sku) {
        return store.findProduct(sku).orElseThrow().stock();
    }

    void close() {
        Schema.drop(name);
    }
}

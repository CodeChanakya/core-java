package com.codechanakya.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private ShopFixture shop;
    private OrderService service;

    @BeforeEach
    void setUp() {
        shop = new ShopFixture();
        service = shop.service(new FakeGateway());
    }

    @AfterEach
    void tearDown() {
        shop.close();
    }

    @Test
    @DisplayName("an order is priced from the catalogue, not from the caller")
    void priced() {
        Order o = service.place(shop.asha.id(), Map.of("BK-1", 2, "TY-1", 4), null);
        assertEquals(new BigDecimal("400.00"), o.subtotal());
        assertEquals(new BigDecimal("400.00"), o.total());
        assertEquals(OrderStatus.NEW, o.status());
    }

    @Test
    @DisplayName("bad orders are refused with a clear message")
    void refused() {
        assertEquals("An order needs at least one item",
                assertThrows(ShopException.class, () -> service.place(shop.asha.id(), Map.of(), null)).getMessage());
        assertEquals("No customer with id 77",
                assertThrows(ShopException.class, () -> service.place(77, Map.of("BK-1", 1), null)).getMessage());
        assertEquals("No product nope",
                assertThrows(ShopException.class, () -> service.place(shop.asha.id(), Map.of("nope", 1), null)).getMessage());
        assertEquals("Quantity for BK-1 must be positive, but was -2",
                assertThrows(ShopException.class, () -> service.place(shop.asha.id(), Map.of("BK-1", -2), null)).getMessage());
    }

    @Test
    @DisplayName("the last item can be bought only once")
    void lastItem() {
        service.place(shop.asha.id(), Map.of("EL-1", 3), null);
        assertThrows(OutOfStockException.class, () -> service.place(shop.bilal.id(), Map.of("EL-1", 1), null));
        assertEquals(0, shop.stock("EL-1"));
    }

    @Test
    @DisplayName("the whole life of an order: pay, ship, deliver")
    void lifecycle() {
        Order o = service.place(shop.asha.id(), Map.of("BK-1", 1), null);
        service.pay(o.id(), "4111");
        service.ship(o.id());
        service.deliver(o.id());
        assertEquals(OrderStatus.DELIVERED, service.order(o.id()).status());
    }

    @Test
    @DisplayName("a declined payment leaves the order NEW")
    void declined() {
        Order o = service.place(shop.asha.id(), Map.of("BK-1", 1), null);
        assertThrows(ShopException.class, () -> service.pay(o.id(), "0000"));
        assertEquals(OrderStatus.NEW, service.order(o.id()).status());
    }

    @Test
    @DisplayName("the gateway is asked for exactly the order total")
    void gatewayAmount() {
        BigDecimal[] charged = new BigDecimal[1];
        OrderService spying = shop.service((card, amount) -> {
            charged[0] = amount;
            return true;
        });
        Order o = spying.place(shop.asha.id(), Map.of("BK-1", 3), "WELCOME5");
        spying.pay(o.id(), "4111");
        assertEquals(new BigDecimal("285.00"), charged[0]);
    }

    @Test
    @DisplayName("steps out of order are refused")
    void badMoves() {
        Order o = service.place(shop.asha.id(), Map.of("BK-1", 1), null);
        assertThrows(ShopException.class, () -> service.ship(o.id()));
        assertThrows(ShopException.class, () -> service.deliver(o.id()));
        service.pay(o.id(), "4111");
        assertThrows(ShopException.class, () -> service.pay(o.id(), "4111"));
    }

    @Test
    @DisplayName("cancelling a paid order gives the stock back; a shipped order cannot be cancelled")
    void cancel() {
        Order paid = service.place(shop.asha.id(), Map.of("BK-1", 5), null);
        service.pay(paid.id(), "4111");
        service.cancel(paid.id());
        assertEquals(20, shop.stock("BK-1"));
        Order shipped = service.place(shop.asha.id(), Map.of("BK-1", 1), null);
        service.pay(shipped.id(), "4111");
        service.ship(shipped.id());
        assertThrows(ShopException.class, () -> service.cancel(shipped.id()));
        assertEquals(19, shop.stock("BK-1"));
    }

    @Test
    @DisplayName("an unknown order id is reported")
    void unknownOrder() {
        assertEquals("No order with id 5", assertThrows(ShopException.class, () -> service.pay(5, "4111")).getMessage());
    }
}

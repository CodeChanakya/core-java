package com.codechanakya.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderStatusTest {

    @Test
    void forwardMovesAreAllowed() {
        assertTrue(OrderStatus.NEW.canMoveTo(OrderStatus.PAID));
        assertTrue(OrderStatus.PAID.canMoveTo(OrderStatus.SHIPPED));
        assertTrue(OrderStatus.SHIPPED.canMoveTo(OrderStatus.DELIVERED));
    }

    @Test
    void skippingAndGoingBackAreNot() {
        assertFalse(OrderStatus.NEW.canMoveTo(OrderStatus.SHIPPED));
        assertFalse(OrderStatus.SHIPPED.canMoveTo(OrderStatus.PAID));
        assertFalse(OrderStatus.SHIPPED.canMoveTo(OrderStatus.CANCELLED));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "CANCELLED"})
    void endStatesGoNowhere(OrderStatus end) {
        assertEquals(EnumSet.noneOf(OrderStatus.class), end.next());
    }
}

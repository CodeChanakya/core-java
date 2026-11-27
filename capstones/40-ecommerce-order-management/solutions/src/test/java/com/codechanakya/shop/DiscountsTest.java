package com.codechanakya.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountsTest {

    private static List<OrderLine> lines(int quantity, String price) {
        return List.of(new OrderLine("A", quantity, new BigDecimal(price)));
    }

    private static BigDecimal subtotal(List<OrderLine> lines) {
        return lines.stream().map(OrderLine::total).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @ParameterizedTest(name = "{0} units at {1}: bulk discount {2}")
    @CsvSource({"9, 100.00, 0.00", "10, 100.00, 100.00", "11, 100.00, 110.00", "20, 12.50, 25.00"})
    void bulk(int quantity, String price, String expected) {
        List<OrderLine> l = lines(quantity, price);
        assertEquals(new BigDecimal(expected), Discounts.BULK.discount(l, subtotal(l), null));
    }

    @ParameterizedTest(name = "subtotal {0}: big order discount {1}")
    @CsvSource({"4999.99, 0.00", "5000.00, 250.00", "10000.00, 500.00"})
    void bigOrder(String subtotal, String expected) {
        assertEquals(new BigDecimal(expected), Discounts.BIG_ORDER.discount(List.of(), new BigDecimal(subtotal), null));
    }

    @ParameterizedTest(name = "coupon {0} on {1}: {2}")
    @CsvSource({"WELCOME5, 1000.00, 50.00", "FLAT100, 999.99, 0.00", "FLAT100, 1000.00, 100.00", "BOGUS, 1000.00, 0.00"})
    void coupons(String coupon, String subtotal, String expected) {
        assertEquals(new BigDecimal(expected), Discounts.COUPON.discount(List.of(), new BigDecimal(subtotal), coupon));
    }

    @Test
    @DisplayName("no coupon at all (null) gives nothing and does not crash")
    void nullCoupon() {
        assertEquals(new BigDecimal("0.00"), Discounts.COUPON.discount(List.of(), new BigDecimal("5000"), null));
    }

    @Test
    @DisplayName("a policy can be written as a one-line lambda")
    void lambdaPolicy() {
        DiscountPolicy fixed = (lines, subtotal, coupon) -> new BigDecimal("7.00");
        assertEquals(new BigDecimal("7.00"), fixed.discount(List.of(), BigDecimal.TEN, null));
    }
}

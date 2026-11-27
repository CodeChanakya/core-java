package com.codechanakya.orders;

public record OrderResult(int orderId, OrderStatus status, String detail) {
}

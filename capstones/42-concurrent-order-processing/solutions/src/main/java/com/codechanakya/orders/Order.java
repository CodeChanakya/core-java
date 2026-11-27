package com.codechanakya.orders;

/** One customer order. Immutable, so it can be handed to any thread safely. */
public record Order(int id, String sku, int quantity, String card, String email) {
}

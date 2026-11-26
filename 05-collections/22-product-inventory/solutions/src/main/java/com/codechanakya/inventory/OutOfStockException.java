package com.codechanakya.inventory;

/** Thrown when a sale asks for more units than are on the shelf. Carries the numbers a caller needs. */
public class OutOfStockException extends Exception {

    private final int requested;
    private final int available;

    public OutOfStockException(String name, int requested, int available) {
        super("Cannot sell " + requested + " of " + name + ", only " + available + " in stock");
        this.requested = requested;
        this.available = available;
    }

    public int getShortfall() {
        return requested - available;
    }
}

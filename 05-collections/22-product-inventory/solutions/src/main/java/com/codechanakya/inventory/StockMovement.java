package com.codechanakya.inventory;

/** One change to stock, kept so the latest one can be undone. Negative means stock went out. */
public class StockMovement {

    private final String sku;
    private final int change;
    private final String reason;

    public StockMovement(String sku, int change, String reason) {
        this.sku = sku;
        this.change = change;
        this.reason = reason;
    }

    public String getSku() {
        return sku;
    }

    public int getChange() {
        return change;
    }

    @Override
    public String toString() {
        return (change > 0 ? "+" : "") + change + " " + sku + " (" + reason + ")";
    }
}

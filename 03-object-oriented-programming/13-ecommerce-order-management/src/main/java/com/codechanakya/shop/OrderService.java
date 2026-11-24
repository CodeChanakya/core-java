package com.codechanakya.shop;

/**
 * The rules for placing an order. Order and Catalog hold data; this class
 * coordinates them, so a rule like "all or nothing" has one clear home.
 */
public class OrderService {

    private final Catalog catalog;
    private int nextOrderNumber = 5001;

    public OrderService(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Builds an order from parallel arrays of SKUs and quantities.
     *
     * It works in two passes. The first only checks, and throws before anything
     * is touched. The second takes stock and builds the order. That way a
     * failure on the third line cannot leave the first two lines already
     * removed from the shelf.
     */
    public Order placeOrder(Customer customer, String[] skus, int[] quantities) {
        for (int i = 0; i < skus.length; i++) {
            Product product = catalog.find(skus[i]);
            if (product == null) {
                throw new IllegalStateException("Unknown product " + skus[i]);
            }
            if (!product.hasStock(quantities[i])) {
                throw new IllegalStateException(
                        "Only " + product.getStock() + " left of " + product.getName()
                        + " (you asked for " + quantities[i] + ")");
            }
        }

        Order order = new Order("ORD-" + nextOrderNumber, customer);
        nextOrderNumber++;
        for (int i = 0; i < skus.length; i++) {
            Product product = catalog.find(skus[i]);
            product.removeStock(quantities[i]);
            order.addItem(product, quantities[i]);
        }
        return order;
    }
}

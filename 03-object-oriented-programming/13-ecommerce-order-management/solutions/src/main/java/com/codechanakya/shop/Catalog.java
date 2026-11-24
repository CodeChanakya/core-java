package com.codechanakya.shop;

import java.util.ArrayList;

/** Every product the shop sells, looked up by SKU. */
public class Catalog {

    private final ArrayList<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    /** Returns the product with this SKU, or null when there is none. */
    public Product find(String sku) {
        for (Product product : products) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        return null;
    }

    /** I1: lists products with this many units or fewer. */
    public void printLowStock(int threshold) {
        System.out.println("--- Low stock (" + threshold + " or fewer) ---");
        for (Product product : products) {
            if (product.getStock() <= threshold) {
                System.out.println(product.getName() + ": " + product.getStock());
            }
        }
    }

    public void printStock() {
        System.out.println("--- Stock ---");
        for (Product product : products) {
            System.out.println(String.format("%-8s %-22s %-8s %5d left",
                    product.getSku(), product.getName(), product.kind(), product.getStock()));
        }
    }
}

package com.codechanakya.quotes;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** The list of products, looked up by SKU. Read-only once built. */
public class Catalog {

    private final Map<String, Product> bySku = new TreeMap<>();

    public Catalog(Collection<Product> products) {
        for (Product product : products) {
            if (bySku.put(product.sku(), product) != null) {
                throw new IllegalArgumentException("Duplicate SKU in catalog: " + product.sku());
            }
        }
    }

    /** Finds a product, or throws {@link UnknownProductException}. */
    public Product get(String sku) {
        Product product = bySku.get(sku);
        if (product == null) {
            throw new UnknownProductException(sku);
        }
        return product;
    }

    /** B1: the products from cheapest to dearest. */
    public List<Product> byPrice() {
        return bySku.values().stream().sorted(Comparator.comparing(Product::unitPrice).thenComparing(Product::sku)).toList();
    }

    public int size() {
        return bySku.size();
    }
}

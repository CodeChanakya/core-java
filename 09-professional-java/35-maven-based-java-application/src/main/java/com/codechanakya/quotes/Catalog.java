package com.codechanakya.quotes;

import java.util.Collection;
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

    public int size() {
        return bySku.size();
    }
}

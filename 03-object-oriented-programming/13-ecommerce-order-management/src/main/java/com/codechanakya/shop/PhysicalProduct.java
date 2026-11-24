package com.codechanakya.shop;

import java.math.BigDecimal;

/** A product that has to be posted. Heavier items cost more to ship. */
public class PhysicalProduct extends Product {

    private static final BigDecimal BASE_SHIPPING = new BigDecimal("30.00");
    private static final BigDecimal PER_KG = new BigDecimal("10.00");

    private final int weightKg;

    public PhysicalProduct(String sku, String name, BigDecimal price, int stock, int weightKg) {
        super(sku, name, price, stock);
        this.weightKg = weightKg;
    }

    @Override
    public BigDecimal shippingPerUnit() {
        return BASE_SHIPPING.add(PER_KG.multiply(BigDecimal.valueOf(weightKg)));
    }

    @Override
    public String kind() {
        return "Physical";
    }
}

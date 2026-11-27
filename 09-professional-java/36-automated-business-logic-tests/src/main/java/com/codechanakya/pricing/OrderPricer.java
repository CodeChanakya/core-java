package com.codechanakya.pricing;

import java.math.BigDecimal;

/**
 * Combines the rules into one price.
 *
 * <p>Order of work: tier discount first, then the coupon on what is left, then 18 percent tax on
 * the discounted goods, and finally shipping (which is not taxed here). The discounts together can
 * never take the goods below zero.
 */
public class OrderPricer {

    static final BigDecimal TAX_RATE = new BigDecimal("0.18");

    private final CouponService coupons;
    private final ShippingCalculator shipping;

    public OrderPricer(CouponService coupons, ShippingCalculator shipping) {
        this.coupons = coupons;
        this.shipping = shipping;
    }

    public PriceBreakdown price(BigDecimal subtotal, CustomerTier tier, String couponCode,
                                int weightGrams, boolean express) {
        if (subtotal.signum() <= 0) {
            throw new IllegalArgumentException("Subtotal must be positive, but was " + subtotal);
        }
        BigDecimal tierDiscount = Money.round(subtotal.multiply(tier.discountRate()));
        BigDecimal afterTier = subtotal.subtract(tierDiscount);

        BigDecimal couponDiscount = coupons.discountFor(couponCode, afterTier).min(afterTier);
        BigDecimal goods = afterTier.subtract(couponDiscount);

        BigDecimal tax = Money.round(goods.multiply(TAX_RATE));
        BigDecimal delivery = shipping.cost(subtotal, weightGrams, express);
        BigDecimal total = goods.add(tax).add(delivery);
        return new PriceBreakdown(Money.round(subtotal), tierDiscount, couponDiscount, tax, delivery, Money.round(total));
    }
}

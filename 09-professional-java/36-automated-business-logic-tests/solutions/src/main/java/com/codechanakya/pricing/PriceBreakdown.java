package com.codechanakya.pricing;

import java.math.BigDecimal;

/** Every number that went into an order's final price. */
public record PriceBreakdown(BigDecimal subtotal, BigDecimal tierDiscount, BigDecimal couponDiscount,
                             BigDecimal tax, BigDecimal shipping, BigDecimal total) {
}

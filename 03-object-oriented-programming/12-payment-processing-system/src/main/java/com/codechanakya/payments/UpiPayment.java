package com.codechanakya.payments;

import java.math.BigDecimal;

/** A UPI id. This demo caps a single payment at 100000.00. */
public class UpiPayment implements PaymentMethod {

    private static final BigDecimal PER_PAYMENT_LIMIT = new BigDecimal("100000.00");

    private final String upiId;
    private int chargeCount = 0;

    public UpiPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public String name() {
        return "UPI " + upiId;
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        if (amount.compareTo(PER_PAYMENT_LIMIT) > 0) {
            return PaymentResult.declined("Above the per-payment limit of " + PER_PAYMENT_LIMIT.toPlainString());
        }
        chargeCount++;
        return PaymentResult.approved(String.format("UPI-%04d", chargeCount));
    }
}

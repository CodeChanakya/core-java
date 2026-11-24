package com.codechanakya.payments;

import java.math.BigDecimal;

/** I1: a fourth way to pay. Adding it required no change to PaymentProcessor. */
public class NetBankingPayment implements PaymentMethod {

    private final String bankName;
    private int chargeCount = 0;

    public NetBankingPayment(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String name() {
        return "NetBanking " + bankName;
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        chargeCount++;
        return PaymentResult.approved(String.format("NET-%04d", chargeCount));
    }
}

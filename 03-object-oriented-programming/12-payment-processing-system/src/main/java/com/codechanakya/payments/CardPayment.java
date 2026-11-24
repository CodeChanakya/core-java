package com.codechanakya.payments;

import java.math.BigDecimal;

/** A credit card with a spending limit that shrinks as it is used. */
public class CardPayment implements PaymentMethod {

    private final String last4;
    private BigDecimal remainingLimit;
    private int chargeCount = 0;

    public CardPayment(String last4, BigDecimal limit) {
        this.last4 = last4;
        this.remainingLimit = limit;
    }

    @Override
    public String name() {
        return "Card ****" + last4;
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        if (amount.compareTo(remainingLimit) > 0) {
            return PaymentResult.declined("Over the card limit (" + remainingLimit.toPlainString() + " left)");
        }
        remainingLimit = remainingLimit.subtract(amount);
        chargeCount++;
        return PaymentResult.approved(String.format("CARD-%04d", chargeCount));
    }

    public BigDecimal getRemainingLimit() {
        return remainingLimit;
    }
}

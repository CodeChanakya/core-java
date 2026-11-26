package com.codechanakya.reporting;

import java.math.BigDecimal;

/** C1: an amount that always travels with its currency, so rupees and dollars cannot be added by accident. */
public record Money(BigDecimal amount, String currency) {

    public Money {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a three-letter code");
        }
    }

    public Money plus(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add " + other.currency + " to " + currency);
        }
        return new Money(amount.add(other.amount), currency);
    }
}

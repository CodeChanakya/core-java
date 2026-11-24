package com.codechanakya.bank;

import java.math.BigDecimal;

/**
 * Thrown when a withdrawal is larger than the balance.
 *
 * This one is checked (it extends Exception). Running out of money is a normal
 * business situation, not a bug, so Java makes every caller decide what to do
 * about it: catch it, or declare it with throws. It also carries data, so the
 * handler can say exactly how much is missing.
 */
public class InsufficientFundsException extends Exception {

    private final BigDecimal requested;
    private final BigDecimal available;

    public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Cannot withdraw " + requested.toPlainString() + ", balance is " + available.toPlainString());
        this.requested = requested;
        this.available = available;
    }

    public BigDecimal getShortfall() {
        return requested.subtract(available);
    }
}

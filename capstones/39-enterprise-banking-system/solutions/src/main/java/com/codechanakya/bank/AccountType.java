package com.codechanakya.bank;

import java.math.BigDecimal;

/**
 * The kinds of account the bank offers, and the lowest balance each may reach.
 *
 * <p>A savings account must never fall below its minimum balance. A current account may go
 * into overdraft, which is a negative balance, down to its overdraft limit.
 */
public enum AccountType {

    SAVINGS("500.00", "0.00"),
    CURRENT("0.00", "10000.00");

    private final BigDecimal minimumBalance;
    private final BigDecimal overdraftLimit;

    AccountType(String minimumBalance, String overdraftLimit) {
        this.minimumBalance = new BigDecimal(minimumBalance);
        this.overdraftLimit = new BigDecimal(overdraftLimit);
    }

    /** The lowest balance this kind of account may hold. Negative for an overdraft. */
    public BigDecimal floor() {
        return minimumBalance.subtract(overdraftLimit);
    }

    public BigDecimal minimumBalance() {
        return minimumBalance;
    }
}

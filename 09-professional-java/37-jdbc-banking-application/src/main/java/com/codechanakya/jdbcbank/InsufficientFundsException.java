package com.codechanakya.jdbcbank;

import java.math.BigDecimal;

/** The paying account does not hold enough money. */
public class InsufficientFundsException extends BankException {

    public InsufficientFundsException(int accountId, BigDecimal amount) {
        super("Account " + accountId + " cannot pay " + amount.toPlainString());
    }
}

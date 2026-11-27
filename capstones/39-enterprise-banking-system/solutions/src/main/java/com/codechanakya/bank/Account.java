package com.codechanakya.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** One customer account. Immutable: a balance change produces a new Account. */
public record Account(int id, String customer, AccountType type, BigDecimal balance) {

    public Account {
        balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public Account withBalance(BigDecimal newBalance) {
        return new Account(id, customer, type, newBalance);
    }
}

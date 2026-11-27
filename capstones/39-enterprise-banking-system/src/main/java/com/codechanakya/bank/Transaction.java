package com.codechanakya.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * One line in an account's history.
 *
 * @param amount       signed: positive when money came in, negative when it went out
 * @param balanceAfter the balance right after this movement
 */
public record Transaction(int id, int accountId, TransactionKind kind, BigDecimal amount,
                          BigDecimal balanceAfter, String note, LocalDate date) {

    public Transaction {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        balanceAfter = balanceAfter.setScale(2, RoundingMode.HALF_UP);
    }
}

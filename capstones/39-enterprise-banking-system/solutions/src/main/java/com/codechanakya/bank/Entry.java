package com.codechanakya.bank;

import java.math.BigDecimal;

/** A single movement of money, to be posted to one account. The amount is signed. */
public record Entry(int accountId, TransactionKind kind, BigDecimal amount, String note) {
}

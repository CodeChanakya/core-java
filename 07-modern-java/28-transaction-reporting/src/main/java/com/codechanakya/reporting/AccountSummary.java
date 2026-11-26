package com.codechanakya.reporting;

import java.math.BigDecimal;

/** A small record for a report row. Records are cheap enough to create for every result like this. */
public record AccountSummary(String account, int count, BigDecimal deposits, BigDecimal withdrawals) {

    public BigDecimal net() {
        return deposits.subtract(withdrawals);
    }
}

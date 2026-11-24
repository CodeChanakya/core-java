package com.codechanakya.bank;

import java.math.BigDecimal;

/** I1: a single withdrawal above the per-transaction limit. Checked, like InsufficientFundsException. */
public class LimitExceededException extends Exception {

    public LimitExceededException(BigDecimal requested, BigDecimal limit) {
        super("Cannot withdraw " + requested.toPlainString() + " at once, the limit is " + limit.toPlainString());
    }
}

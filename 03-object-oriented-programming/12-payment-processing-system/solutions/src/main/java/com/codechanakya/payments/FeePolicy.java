package com.codechanakya.payments;

import java.math.BigDecimal;

/** A rule for the fee added on top of a payment. A second small interface the processor depends on. */
public interface FeePolicy {

    BigDecimal feeFor(BigDecimal amount);

    /** Text for receipts, such as "2% fee". */
    String describe();
}

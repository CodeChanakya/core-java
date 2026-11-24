package com.codechanakya.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** The same fee for every payment. A flat fee of zero means "free". */
public class FlatFee implements FeePolicy {

    private final BigDecimal fee;

    public FlatFee(BigDecimal fee) {
        this.fee = fee.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal feeFor(BigDecimal amount) {
        return fee;
    }

    @Override
    public String describe() {
        return "flat " + fee.toPlainString();
    }
}

package com.codechanakya.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Takes payments using whichever method and fee policy it was given.
 *
 * This is composition: the processor HAS a PaymentMethod and HAS a FeePolicy,
 * instead of BEING a card processor or a UPI processor. We could have written
 * CardProcessor, UpiProcessor, WalletProcessor and every combination with
 * every fee rule, and ended up with a pile of near-identical classes. With
 * composition there is one processor, and the parts plug in.
 */
public class PaymentProcessor {

    private PaymentMethod method;
    private FeePolicy feePolicy;
    private final ArrayList<String> ledger = new ArrayList<>();

    public PaymentProcessor(PaymentMethod method, FeePolicy feePolicy) {
        this.method = method;
        this.feePolicy = feePolicy;
    }

    /** Swap how customers pay, without touching anything else. */
    public void useMethod(PaymentMethod method) {
        this.method = method;
    }

    public void useFeePolicy(FeePolicy feePolicy) {
        this.feePolicy = feePolicy;
    }

    /** Charges the amount plus the fee, and records the outcome in the ledger. */
    public PaymentResult pay(BigDecimal amount) {
        if (amount.signum() <= 0) {
            PaymentResult refused = PaymentResult.declined("Amount must be positive");
            ledger.add("DECLINED  " + method.name() + ": " + refused.getMessage());
            return refused;
        }
        BigDecimal fee = feePolicy.feeFor(amount);
        BigDecimal total = amount.add(fee);
        PaymentResult result = method.charge(total);

        if (result.isApproved()) {
            ledger.add(String.format("APPROVED %s charged %s (amount %s + %s) via %s",
                    result.getReference(), total.toPlainString(), amount.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    feePolicy.describe(), method.name()));
        } else {
            ledger.add("DECLINED  " + method.name() + ": " + result.getMessage());
        }
        return result;
    }

    public void printLedger() {
        System.out.println("--- Ledger ---");
        for (String line : ledger) {
            System.out.println(line);
        }
    }
}

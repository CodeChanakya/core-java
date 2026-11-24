package com.codechanakya.payments;

import java.math.BigDecimal;

/**
 * Anything that can take a payment: a card, a UPI id, a wallet.
 *
 * The processor only ever sees this interface. It does not know, or care,
 * which concrete class is behind it, so a new way to pay is a new class and
 * the processor does not change.
 */
public interface PaymentMethod {

    /** A short label for receipts, such as "Card ****4242". */
    String name();

    /** Tries to take this amount. Never throws for a normal refusal; it returns a declined result. */
    PaymentResult charge(BigDecimal amount);
}

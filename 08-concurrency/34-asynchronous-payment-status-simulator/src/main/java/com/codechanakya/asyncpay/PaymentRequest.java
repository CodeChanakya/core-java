package com.codechanakya.asyncpay;

import java.math.BigDecimal;

/**
 * A request to pay. Immutable, so any number of threads can read it without a lock.
 *
 * @param id      the payment's reference
 * @param account the account the money comes from
 * @param amount  how much, in rupees
 * @param method  card, upi or wallet
 */
public record PaymentRequest(String id, String account, BigDecimal amount, String method) {

    public static PaymentRequest of(String id, String account, String amount, String method) {
        return new PaymentRequest(id, account, new BigDecimal(amount), method);
    }
}

package com.codechanakya.asyncpay;

/** The request itself is wrong, for example a zero amount. Retrying will not help. */
public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(String message) {
        super(message);
    }
}

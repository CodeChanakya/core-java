package com.codechanakya.asyncpay;

/** A business rule said no. The request was fine; the answer is simply "not this one". */
public class DeclinedException extends RuntimeException {

    public DeclinedException(String message) {
        super(message);
    }
}

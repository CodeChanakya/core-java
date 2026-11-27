package com.codechanakya.asyncpay;

/** How a payment ended. */
public enum Status {
    COMPLETED,      // money moved and the customer was told (or we tried)
    DECLINED,       // a business rule said no: not enough money, or too risky
    FAILED,         // something was wrong with the request, or the gateway broke
    TIMED_OUT       // the gateway took too long to answer
}

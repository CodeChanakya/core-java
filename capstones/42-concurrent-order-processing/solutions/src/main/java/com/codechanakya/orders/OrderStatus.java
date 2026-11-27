package com.codechanakya.orders;

/** How an order ended. Every order ends in exactly one of these. */
public enum OrderStatus {
    COMPLETED, OUT_OF_STOCK, DECLINED, TIMED_OUT, FAILED
}

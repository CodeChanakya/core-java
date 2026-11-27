package com.codechanakya.asyncpay;

/** The payment gateway is down. Unlike an invalid request, this one may work if tried again. */
public class GatewayUnavailableException extends RuntimeException {

    public GatewayUnavailableException(String message) {
        super(message);
    }
}

package com.codechanakya.hr;

/** Everything the HR system refuses on purpose. */
public class HrException extends RuntimeException {

    public HrException(String message) {
        super(message);
    }

    public HrException(String message, Throwable cause) {
        super(message, cause);
    }
}

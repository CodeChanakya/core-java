package com.codechanakya.empdb;

/** Base class for every problem this program reports on purpose. */
public class EmployeeException extends RuntimeException {

    public EmployeeException(String message) {
        super(message);
    }

    public EmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}

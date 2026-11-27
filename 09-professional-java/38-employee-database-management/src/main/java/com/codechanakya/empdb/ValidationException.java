package com.codechanakya.empdb;

/** The data given for an employee breaks a business rule. */
public class ValidationException extends EmployeeException {

    public ValidationException(String message) {
        super(message);
    }
}

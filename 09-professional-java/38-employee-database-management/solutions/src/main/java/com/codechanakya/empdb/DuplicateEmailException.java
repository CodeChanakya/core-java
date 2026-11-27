package com.codechanakya.empdb;

/** Another employee already uses this e-mail address. */
public class DuplicateEmailException extends EmployeeException {

    public DuplicateEmailException(String email) {
        super("E-mail already in use: " + email);
    }
}

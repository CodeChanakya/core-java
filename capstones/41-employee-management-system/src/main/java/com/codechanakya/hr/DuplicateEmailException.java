package com.codechanakya.hr;

public class DuplicateEmailException extends HrException {

    public DuplicateEmailException(String email) {
        super("An employee with the email " + email + " already exists");
    }
}

package com.codechanakya.empdb;

/** No employee has the given id. */
public class EmployeeNotFoundException extends EmployeeException {

    public EmployeeNotFoundException(int id) {
        super("No employee with id " + id);
    }
}

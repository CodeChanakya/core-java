package com.codechanakya.empdb;

/** Runs every promise in the contract against the in-memory implementation. */
class InMemoryEmployeeRepositoryTest extends EmployeeRepositoryContract {

    @Override
    protected EmployeeRepository create() {
        return new InMemoryEmployeeRepository();
    }
}

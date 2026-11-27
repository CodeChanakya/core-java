package com.codechanakya.empdb;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;

/** Runs every promise in the contract against a real (in-memory) Derby database. */
class JdbcEmployeeRepositoryTest extends EmployeeRepositoryContract {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private String databaseName;

    @Override
    protected EmployeeRepository create() {
        // A new database name per test means tests can never see each other's rows.
        databaseName = "test" + COUNTER.incrementAndGet();
        ConnectionProvider provider = Schema.inMemory(databaseName);
        Schema.create(provider);
        return new JdbcEmployeeRepository(provider);
    }

    @AfterEach
    void dropDatabase() {
        Schema.drop(databaseName);
    }
}

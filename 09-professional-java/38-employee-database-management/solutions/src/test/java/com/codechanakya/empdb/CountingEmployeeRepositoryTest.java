package com.codechanakya.empdb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** C1: the decorator keeps every promise of the contract, and counts calls on top of that. */
class CountingEmployeeRepositoryTest extends EmployeeRepositoryContract {

    @Override
    protected EmployeeRepository create() {
        return new CountingEmployeeRepository(new InMemoryEmployeeRepository());
    }

    @Test
    void countsEveryCall() {
        CountingEmployeeRepository counting = (CountingEmployeeRepository) repository;
        counting.insert(draft("A", "a@example.com", Department.QA, "40000"));
        counting.findAll();
        counting.findAll();
        assertEquals(Map.of("insert", 1, "findAll", 2), counting.callCounts());
    }
}

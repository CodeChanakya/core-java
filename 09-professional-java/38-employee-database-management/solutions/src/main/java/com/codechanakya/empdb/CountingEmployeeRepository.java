package com.codechanakya.empdb;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * C1: a decorator. It looks like an EmployeeRepository, passes every call to a real one, and counts the calls.
 * Nothing else in the program had to change to allow this (the O in SOLID: open for extension).
 */
public class CountingEmployeeRepository implements EmployeeRepository {

    private final EmployeeRepository delegate;
    private final Map<String, Integer> calls = new TreeMap<>();

    public CountingEmployeeRepository(EmployeeRepository delegate) {
        this.delegate = delegate;
    }

    /** How many times each method was called, ordered by method name. */
    public Map<String, Integer> callCounts() {
        return Map.copyOf(calls);
    }

    private void count(String method) {
        calls.merge(method, 1, Integer::sum);
    }

    @Override
    public Employee insert(Employee draft) {
        count("insert");
        return delegate.insert(draft);
    }

    @Override
    public void update(Employee employee) {
        count("update");
        delegate.update(employee);
    }

    @Override
    public boolean delete(int id) {
        count("delete");
        return delegate.delete(id);
    }

    @Override
    public Optional<Employee> findById(int id) {
        count("findById");
        return delegate.findById(id);
    }

    @Override
    public List<Employee> findAll() {
        count("findAll");
        return delegate.findAll();
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        count("findByDepartment");
        return delegate.findByDepartment(department);
    }

    @Override
    public List<Employee> findByNamePrefix(String prefix) {
        count("findByNamePrefix");
        return delegate.findByNamePrefix(prefix);
    }

    @Override
    public boolean existsByEmail(String email) {
        count("existsByEmail");
        return delegate.existsByEmail(email);
    }

    @Override
    public Map<Department, DepartmentStats> statsByDepartment() {
        count("statsByDepartment");
        return delegate.statsByDepartment();
    }
}

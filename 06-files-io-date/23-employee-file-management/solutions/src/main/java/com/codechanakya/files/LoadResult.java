package com.codechanakya.files;

import java.util.ArrayList;
import java.util.List;

/**
 * What came out of reading a file: the good rows, and a note about every row that was rejected.
 *
 * Real files are never perfectly clean. Returning both lists lets the caller decide
 * whether a few bad rows are acceptable, instead of the loader deciding for everyone.
 */
public class LoadResult {

    private final List<Employee> employees = new ArrayList<>();
    private final List<String> problems = new ArrayList<>();

    void addEmployee(Employee employee) {
        employees.add(employee);
    }

    void addProblem(String problem) {
        problems.add(problem);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<String> getProblems() {
        return problems;
    }
}

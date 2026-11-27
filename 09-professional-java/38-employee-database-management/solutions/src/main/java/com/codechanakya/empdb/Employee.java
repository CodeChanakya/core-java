package com.codechanakya.empdb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * One employee. Immutable: a change produces a new object with {@code withSalary} or {@code withDepartment}.
 *
 * <p>An id of {@code 0} means "not stored yet". The database hands out the real id on insert.
 * The salary is always kept with exactly two decimals, so two employees with the same pay are equal
 * whether the number came from Java or from the database.
 */
public record Employee(int id, String name, String email, Department department, BigDecimal salary, LocalDate hired) {

    public Employee {
        salary = salary.setScale(2, RoundingMode.HALF_UP);
    }

    /** A new employee that has not been stored yet. */
    public static Employee draft(String name, String email, Department department, BigDecimal salary, LocalDate hired) {
        return new Employee(0, name, email, department, salary, hired);
    }

    public Employee withId(int newId) {
        return new Employee(newId, name, email, department, salary, hired);
    }

    public Employee withSalary(BigDecimal newSalary) {
        return new Employee(id, name, email, department, newSalary, hired);
    }

    public Employee withDepartment(Department newDepartment) {
        return new Employee(id, name, email, newDepartment, salary, hired);
    }
}

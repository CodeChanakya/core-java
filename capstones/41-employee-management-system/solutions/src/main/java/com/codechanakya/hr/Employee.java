package com.codechanakya.hr;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/** One person who works (or worked) at the company. {@code active} is false after they leave. */
public record Employee(int id, String name, String email, Department department, LocalDate birthDate,
                       LocalDate joinDate, BigDecimal monthlySalary, boolean active) {

    public Employee {
        monthlySalary = monthlySalary.setScale(2, RoundingMode.HALF_UP);
    }

    public Employee withSalary(BigDecimal newSalary) {
        return new Employee(id, name, email, department, birthDate, joinDate, newSalary, active);
    }

    public Employee withDepartment(Department newDepartment) {
        return new Employee(id, name, email, newDepartment, birthDate, joinDate, monthlySalary, active);
    }

    public Employee deactivated() {
        return new Employee(id, name, email, department, birthDate, joinDate, monthlySalary, false);
    }
}

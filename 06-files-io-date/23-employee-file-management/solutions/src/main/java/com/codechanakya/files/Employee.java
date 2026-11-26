package com.codechanakya.files;

import java.math.BigDecimal;

/** One row of the employee file. Immutable, so a loaded record cannot drift from the file. */
public class Employee {

    private final String id;
    private final String name;
    private final String department;
    private final BigDecimal salary;

    public Employee(String id, String name, String department, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    /** The line to write back to a CSV file. Names with commas would need quoting; this file has none. */
    public String toCsv() {
        return id + "," + name + "," + department + "," + salary.toPlainString();
    }
}

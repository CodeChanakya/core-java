package com.codechanakya.directory;

import java.util.Objects;

/**
 * An employee, identified by employee id.
 *
 * equals() and hashCode() are written together and from the same field. Two Employee
 * objects with the same id are the same person, and they must also produce the
 * same hash code, otherwise a HashMap or HashSet cannot find them again.
 */
public class Employee {

    private final String id;
    private final String name;
    private final String department;

    public Employee(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Employee)) {
            return false;
        }
        Employee that = (Employee) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " " + name + " (" + department + ")";
    }
}

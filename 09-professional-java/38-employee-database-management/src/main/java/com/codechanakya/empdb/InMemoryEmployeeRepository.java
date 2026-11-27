package com.codechanakya.empdb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** Keeps employees in a map. Used by fast tests, and as a second implementation of the interface. */
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Integer, Employee> rows = new TreeMap<>();
    private int nextId = 1;

    @Override
    public Employee insert(Employee draft) {
        requireUniqueEmail(draft.email(), 0);
        Employee stored = draft.withId(nextId++);
        rows.put(stored.id(), stored);
        return stored;
    }

    @Override
    public void update(Employee employee) {
        if (!rows.containsKey(employee.id())) {
            throw new EmployeeNotFoundException(employee.id());
        }
        requireUniqueEmail(employee.email(), employee.id());
        rows.put(employee.id(), employee);
    }

    @Override
    public boolean delete(int id) {
        return rows.remove(id) != null;
    }

    @Override
    public Optional<Employee> findById(int id) {
        return Optional.ofNullable(rows.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(rows.values());
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        return rows.values().stream().filter(e -> e.department() == department)
                .sorted(Comparator.comparingInt(Employee::id)).toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return rows.values().stream().anyMatch(e -> e.email().toLowerCase(Locale.ROOT).equals(email.toLowerCase(Locale.ROOT)));
    }

    @Override
    public Map<Department, DepartmentStats> statsByDepartment() {
        Map<Department, DepartmentStats> stats = new EnumMap<>(Department.class);
        for (Employee employee : rows.values()) {
            DepartmentStats old = stats.get(employee.department());
            stats.put(employee.department(), old == null
                    ? new DepartmentStats(1, employee.salary(), employee.salary())
                    : new DepartmentStats(old.headcount() + 1, old.payroll().add(employee.salary()),
                            old.highest().max(employee.salary())));
        }
        return stats;
    }

    private void requireUniqueEmail(String email, int ownId) {
        boolean taken = rows.values().stream()
                .anyMatch(e -> e.id() != ownId && e.email().equalsIgnoreCase(email));
        if (taken) {
            throw new DuplicateEmailException(email);
        }
    }
}

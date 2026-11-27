package com.codechanakya.empdb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The business rules. Knows nothing about SQL: it talks to an {@link EmployeeRepository} and to a {@link Clock}.
 *
 * <p>Both are handed in through the constructor (dependency injection). A test passes an in-memory
 * repository and a clock frozen on one day, and the rules can be checked without a database or a calendar.
 */
public class EmployeeService {

    static final BigDecimal MINIMUM_SALARY = new BigDecimal("15000.00");
    static final BigDecimal MAXIMUM_RAISE_PERCENT = new BigDecimal("20");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final EmployeeRepository repository;
    private final Clock clock;

    public EmployeeService(EmployeeRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Employee hire(String name, String email, Department department, BigDecimal salary, LocalDate hired) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required.");
        }
        if (email == null || !EMAIL.matcher(email).matches()) {
            throw new ValidationException("Not a valid e-mail address: " + email);
        }
        if (salary.compareTo(MINIMUM_SALARY) < 0) {
            throw new ValidationException("Salary must be at least " + MINIMUM_SALARY.toPlainString() + ", but was "
                    + salary.setScale(2, RoundingMode.HALF_UP).toPlainString());
        }
        if (hired.isAfter(LocalDate.now(clock))) {
            throw new ValidationException("Hire date " + hired + " is in the future.");
        }
        if (repository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        return repository.insert(Employee.draft(name.trim(), email.trim(), department, salary, hired));
    }

    public Employee giveRaise(int id, BigDecimal percent) {
        if (percent.signum() <= 0 || percent.compareTo(MAXIMUM_RAISE_PERCENT) > 0) {
            throw new ValidationException("A raise must be above 0 and at most " + MAXIMUM_RAISE_PERCENT + " percent, but was "
                    + percent.toPlainString());
        }
        Employee current = get(id);
        BigDecimal factor = BigDecimal.ONE.add(percent.movePointLeft(2));
        Employee raised = current.withSalary(current.salary().multiply(factor));
        repository.update(raised);
        return raised;
    }

    public Employee moveTo(int id, Department department) {
        Employee moved = get(id).withDepartment(department);
        repository.update(moved);
        return moved;
    }

    public void terminate(int id) {
        if (!repository.delete(id)) {
            throw new EmployeeNotFoundException(id);
        }
    }

    public Employee get(int id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<Employee> everyone() {
        return repository.findAll();
    }

    /** A2 */
    public List<Employee> searchByName(String prefix) {
        return repository.findByNamePrefix(prefix);
    }

    /** B1: the average salary of one department. */
    public BigDecimal averageSalary(Department department) {
        DepartmentStats stats = repository.statsByDepartment().get(department);
        if (stats == null) {
            throw new ValidationException("Nobody works in " + department);
        }
        return stats.average();
    }

    public List<Employee> inDepartment(Department department) {
        return repository.findByDepartment(department);
    }

    public Map<Department, DepartmentStats> departmentReport() {
        return repository.statsByDepartment();
    }

    public BigDecimal totalPayroll() {
        return repository.statsByDepartment().values().stream()
                .map(DepartmentStats::payroll).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }
}

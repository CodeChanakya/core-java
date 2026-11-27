package com.codechanakya.empdb;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * What the rest of the program needs from "somewhere that employees are stored".
 *
 * <p>This interface is the line between business rules and storage (the D in SOLID: depend on
 * abstractions). {@code EmployeeService} knows only this interface. A database implementation and an
 * in-memory one both promise the same behaviour, and one shared test class holds both to that promise.
 */
public interface EmployeeRepository {

    /** Stores a draft (id 0) and returns it with the id that was chosen. */
    Employee insert(Employee draft);

    /** Replaces the stored data of an existing employee. */
    void update(Employee employee);

    /** Removes an employee; returns false when there was nobody with that id. */
    boolean delete(int id);

    Optional<Employee> findById(int id);

    /** Everyone, ordered by id. */
    List<Employee> findAll();

    /** People in one department, ordered by id. */
    List<Employee> findByDepartment(Department department);

    /** A2: people whose name starts with the text, ignoring case, ordered by id. */
    List<Employee> findByNamePrefix(String prefix);

    boolean existsByEmail(String email);

    /** Figures for every department that has at least one person, in enum order. */
    Map<Department, DepartmentStats> statsByDepartment();
}

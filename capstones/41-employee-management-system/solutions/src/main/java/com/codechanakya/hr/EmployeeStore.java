package com.codechanakya.hr;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Create, read, update and delete for employees and their leave. */
public interface EmployeeStore {

    /** Saves a new employee and returns it with its generated id. @throws DuplicateEmailException if the email is taken. */
    Employee add(Employee employee);

    Optional<Employee> find(int id);

    /** Everyone, ordered by id. */
    List<Employee> findAll();

    List<Employee> findByDepartment(Department department);

    /** Replaces the stored employee with the same id. */
    void update(Employee employee);

    LeaveRequest addLeave(int employeeId, LeaveType type, LocalDate from, LocalDate to, int days);

    Optional<LeaveRequest> findLeave(int id);

    /** One employee's leave, oldest start date first. */
    List<LeaveRequest> leavesOf(int employeeId);

    void updateLeaveStatus(int leaveId, LeaveStatus status);
}

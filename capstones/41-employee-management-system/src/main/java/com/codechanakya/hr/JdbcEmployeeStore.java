package com.codechanakya.hr;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** The Derby version of {@link EmployeeStore}. Dates travel as {@code java.sql.Date} and come back as {@link LocalDate}. */
public class JdbcEmployeeStore implements EmployeeStore {

    private static final String EMPLOYEE_COLUMNS =
            "id, name, email, department, birth_date, join_date, monthly_salary, active";
    private static final String LEAVE_COLUMNS = "id, employee_id, type, from_date, to_date, days, status";

    private final ConnectionProvider provider;

    public JdbcEmployeeStore(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public Employee add(Employee e) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO employee (name, email, department, birth_date, join_date, monthly_salary, active) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, e.name());
            s.setString(2, e.email());
            s.setString(3, e.department().name());
            s.setDate(4, Date.valueOf(e.birthDate()));
            s.setDate(5, Date.valueOf(e.joinDate()));
            s.setBigDecimal(6, e.monthlySalary());
            s.setBoolean(7, e.active());
            s.executeUpdate();
            try (ResultSet keys = s.getGeneratedKeys()) {
                keys.next();
                return new Employee(keys.getInt(1), e.name(), e.email(), e.department(), e.birthDate(), e.joinDate(),
                        e.monthlySalary(), e.active());
            }
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                throw new DuplicateEmailException(e.email());
            }
            throw translate("Adding an employee", ex);
        }
    }

    @Override
    public Optional<Employee> find(int id) {
        return employees("SELECT " + EMPLOYEE_COLUMNS + " FROM employee WHERE id = ?", id).stream().findFirst();
    }

    @Override
    public List<Employee> findAll() {
        return employees("SELECT " + EMPLOYEE_COLUMNS + " FROM employee ORDER BY id");
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        return employees("SELECT " + EMPLOYEE_COLUMNS + " FROM employee WHERE department = ? ORDER BY id", department.name());
    }

    @Override
    public void update(Employee e) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "UPDATE employee SET name = ?, email = ?, department = ?, birth_date = ?, join_date = ?, monthly_salary = ?, active = ? WHERE id = ?")) {
            s.setString(1, e.name());
            s.setString(2, e.email());
            s.setString(3, e.department().name());
            s.setDate(4, Date.valueOf(e.birthDate()));
            s.setDate(5, Date.valueOf(e.joinDate()));
            s.setBigDecimal(6, e.monthlySalary());
            s.setBoolean(7, e.active());
            s.setInt(8, e.id());
            if (s.executeUpdate() == 0) {
                throw new HrException("No employee with id " + e.id());
            }
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                throw new DuplicateEmailException(e.email());
            }
            throw translate("Updating an employee", ex);
        }
    }

    @Override
    public LeaveRequest addLeave(int employeeId, LeaveType type, LocalDate from, LocalDate to, int days) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO leave_request (employee_id, type, from_date, to_date, days, status) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, employeeId);
            s.setString(2, type.name());
            s.setDate(3, Date.valueOf(from));
            s.setDate(4, Date.valueOf(to));
            s.setInt(5, days);
            s.setString(6, LeaveStatus.PENDING.name());
            s.executeUpdate();
            try (ResultSet keys = s.getGeneratedKeys()) {
                keys.next();
                return new LeaveRequest(keys.getInt(1), employeeId, type, from, to, days, LeaveStatus.PENDING);
            }
        } catch (SQLException ex) {
            throw translate("Adding leave", ex);
        }
    }

    @Override
    public Optional<LeaveRequest> findLeave(int id) {
        return leaves("SELECT " + LEAVE_COLUMNS + " FROM leave_request WHERE id = ?", id).stream().findFirst();
    }

    @Override
    public List<LeaveRequest> leavesOf(int employeeId) {
        return leaves("SELECT " + LEAVE_COLUMNS + " FROM leave_request WHERE employee_id = ? ORDER BY from_date, id", employeeId);
    }

    @Override
    public void updateLeaveStatus(int leaveId, LeaveStatus status) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "UPDATE leave_request SET status = ? WHERE id = ?")) {
            s.setString(1, status.name());
            s.setInt(2, leaveId);
            s.executeUpdate();
        } catch (SQLException ex) {
            throw translate("Updating leave", ex);
        }
    }

    private List<Employee> employees(String sql, Object... params) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, params);
            try (ResultSet r = s.executeQuery()) {
                List<Employee> list = new ArrayList<>();
                while (r.next()) {
                    list.add(new Employee(r.getInt(1), r.getString(2), r.getString(3), Department.valueOf(r.getString(4)),
                            r.getDate(5).toLocalDate(), r.getDate(6).toLocalDate(), r.getBigDecimal(7), r.getBoolean(8)));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw translate("Reading employees", ex);
        }
    }

    private List<LeaveRequest> leaves(String sql, Object... params) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, params);
            try (ResultSet r = s.executeQuery()) {
                List<LeaveRequest> list = new ArrayList<>();
                while (r.next()) {
                    list.add(new LeaveRequest(r.getInt(1), r.getInt(2), LeaveType.valueOf(r.getString(3)),
                            r.getDate(4).toLocalDate(), r.getDate(5).toLocalDate(), r.getInt(6), LeaveStatus.valueOf(r.getString(7))));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw translate("Reading leave", ex);
        }
    }

    private static void bind(PreparedStatement s, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            s.setObject(i + 1, params[i]);
        }
    }

    private static HrException translate(String action, SQLException e) {
        return new HrException(action + " failed (SQLState " + e.getSQLState() + "): " + e.getMessage(), e);
    }
}

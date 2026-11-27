package com.codechanakya.empdb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The database version of {@link EmployeeRepository}.
 *
 * <p>Each method opens a connection, does one thing and closes it (try-with-resources), so there is
 * no connection to leak and no state to keep. Every value goes in through a {@code ?} placeholder.
 */
public class JdbcEmployeeRepository implements EmployeeRepository {

    private static final String COLUMNS = "id, name, email, department, salary, hired";

    private final ConnectionProvider provider;

    public JdbcEmployeeRepository(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public Employee insert(Employee draft) {
        String sql = "INSERT INTO employee (name, email, department, salary, hired) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = provider.open();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, draft);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return draft.withId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw translate("Inserting " + draft.email(), e, draft.email());
        }
    }

    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employee SET name = ?, email = ?, department = ?, salary = ?, hired = ? WHERE id = ?";
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, employee);
            statement.setInt(6, employee.id());
            if (statement.executeUpdate() == 0) {
                throw new EmployeeNotFoundException(employee.id());
            }
        } catch (SQLException e) {
            throw translate("Updating employee " + employee.id(), e, employee.email());
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection connection = provider.open();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM employee WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw translate("Deleting employee " + id, e, null);
        }
    }

    @Override
    public Optional<Employee> findById(int id) {
        return query("SELECT " + COLUMNS + " FROM employee WHERE id = ?", id).stream().findFirst();
    }

    @Override
    public List<Employee> findAll() {
        return query("SELECT " + COLUMNS + " FROM employee ORDER BY id");
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        return query("SELECT " + COLUMNS + " FROM employee WHERE department = ? ORDER BY id", department.name());
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM employee WHERE LOWER(email) = LOWER(?)";
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                return rows.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw translate("Checking e-mail " + email, e, null);
        }
    }

    @Override
    public Map<Department, DepartmentStats> statsByDepartment() {
        // The database does the counting and adding. Only the sums and the maximum are asked for, because
        // those are exact; the average is worked out in Java with a rounding rule we control.
        String sql = "SELECT department, COUNT(*), SUM(salary), MAX(salary) FROM employee GROUP BY department";
        Map<Department, DepartmentStats> stats = new EnumMap<>(Department.class);
        try (Connection connection = provider.open();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                BigDecimal payroll = rows.getBigDecimal(3).setScale(2);
                BigDecimal highest = rows.getBigDecimal(4).setScale(2);
                stats.put(Department.valueOf(rows.getString(1)), new DepartmentStats(rows.getInt(2), payroll, highest));
            }
            return stats;
        } catch (SQLException e) {
            throw translate("Calculating department figures", e, null);
        }
    }

    private List<Employee> query(String sql, Object... parameters) {
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            try (ResultSet rows = statement.executeQuery()) {
                List<Employee> found = new ArrayList<>();
                while (rows.next()) {
                    found.add(new Employee(rows.getInt("id"), rows.getString("name"), rows.getString("email"),
                            Department.valueOf(rows.getString("department")), rows.getBigDecimal("salary"),
                            rows.getDate("hired").toLocalDate()));
                }
                return found;
            }
        } catch (SQLException e) {
            throw translate("Querying employees", e, null);
        }
    }

    private static void bind(PreparedStatement statement, Employee employee) throws SQLException {
        statement.setString(1, employee.name());
        statement.setString(2, employee.email());
        statement.setString(3, employee.department().name());
        statement.setBigDecimal(4, employee.salary());
        statement.setDate(5, Date.valueOf(employee.hired()));
    }

    /** Turns a database error into this program's own exception. SQLState 23505 means a unique key was broken. */
    private static EmployeeException translate(String action, SQLException e, String email) {
        if ("23505".equals(e.getSQLState()) && email != null) {
            return new DuplicateEmailException(email);
        }
        return new EmployeeException(action + " failed (SQLState " + e.getSQLState() + "): " + e.getMessage(), e);
    }
}

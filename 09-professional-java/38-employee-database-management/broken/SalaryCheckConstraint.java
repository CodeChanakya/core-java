import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** The database is the last line of defence: even code that skips EmployeeService cannot store a zero salary. */
public class SalaryCheckConstraint {

    public static void main(String[] args) throws SQLException {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo2;create=true"); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE employee (id INT PRIMARY KEY, salary DECIMAL(12,2) CONSTRAINT salary_positive CHECK (salary > 0))");
            try {
                s.executeUpdate("INSERT INTO employee VALUES (1, 0.00)");
            } catch (SQLException e) {
                System.out.println("SQLState " + e.getSQLState());
                System.out.println(e.getMessage());
            }
        }
    }
}

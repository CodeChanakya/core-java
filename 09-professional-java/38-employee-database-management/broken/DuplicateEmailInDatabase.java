import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** What the database says when a UNIQUE column gets a second copy. JdbcEmployeeRepository turns this into DuplicateEmailException. */
public class DuplicateEmailInDatabase {

    public static void main(String[] args) throws SQLException {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo;create=true"); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE employee (id INT PRIMARY KEY, email VARCHAR(50) CONSTRAINT email_unique UNIQUE)");
            s.executeUpdate("INSERT INTO employee VALUES (1, 'asha@example.com')");
            try {
                s.executeUpdate("INSERT INTO employee VALUES (2, 'asha@example.com')");
            } catch (SQLException e) {
                System.out.println("SQLState " + e.getSQLState());
                System.out.println(e.getMessage());
            }
        }
    }
}

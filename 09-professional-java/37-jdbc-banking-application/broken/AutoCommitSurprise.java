import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A new connection is in auto-commit mode: every statement is its own transaction and becomes permanent
 * the moment it finishes. A transfer made of two statements can therefore fail halfway.
 */
public class AutoCommitSurprise {

    public static void main(String[] args) throws SQLException {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo;create=true"); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE account (id INT PRIMARY KEY, balance INT CONSTRAINT not_negative CHECK (balance >= 0))");
            s.executeUpdate("INSERT INTO account VALUES (1, 100), (2, 100)");

            System.out.println("auto-commit is " + c.getAutoCommit());
            try {
                s.executeUpdate("UPDATE account SET balance = balance - 50 WHERE id = 1");   // permanent at once
                s.executeUpdate("UPDATE account SET balance = balance + 50 WHERE id = 3");   // nobody: changes 0 rows
                s.executeUpdate("UPDATE account SET balance = -1 WHERE id = 2");             // breaks the CHECK rule
            } catch (SQLException e) {
                System.out.println("a later step failed: " + e.getMessage());
            }
            try (ResultSet r = s.executeQuery("SELECT SUM(balance) FROM account")) {
                r.next();
                System.out.println("money in the bank: " + r.getInt(1) + " (it was 200)");
            }
        }
    }
}

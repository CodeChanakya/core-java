import java.sql.*;

/** BROKEN: a transfer written as two separate statements with auto-commit on. */
public class AutoCommitTransfer {
    public static void main(String[] args) throws Exception {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:broken1;create=true");
             Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE account (id INT PRIMARY KEY, balance DECIMAL(10,2) NOT NULL, CONSTRAINT ok CHECK (balance >= 0))");
            s.executeUpdate("INSERT INTO account VALUES (1, 1000), (2, 0)");
            // Auto-commit is on, so every statement is saved the moment it runs.
            try {
                s.executeUpdate("UPDATE account SET balance = balance - 300 WHERE id = 1");
                s.executeUpdate("UPDATE account SET balance = balance + 300 WHERE id = 99");  // no such account: changes nothing
                s.executeUpdate("UPDATE account SET balance = balance - 5000 WHERE id = 2");  // breaks the CHECK rule
            } catch (SQLException e) {
                System.out.println("The transfer failed: " + e.getSQLState());
            }
            try (ResultSet r = s.executeQuery("SELECT SUM(balance) FROM account")) {
                r.next();
                System.out.println("Total money in the bank (should be 1000.00): " + r.getBigDecimal(1));
            }
        }
    }
}

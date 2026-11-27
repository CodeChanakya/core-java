import java.sql.*;

/** BROKEN: SQL built by gluing text together. */
public class ConcatenatedSql {
    public static void main(String[] args) throws Exception {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:broken3;create=true");
             Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE account (id INT PRIMARY KEY, customer VARCHAR(40), balance DECIMAL(10,2))");
            s.executeUpdate("INSERT INTO account VALUES (1, 'Asha', 500), (2, 'Bilal', 900)");
            String typed = "nobody' OR 'a'='a";   // what an attacker types as a customer name
            String sql = "SELECT customer, balance FROM account WHERE customer = '" + typed + "'";
            try (ResultSet r = s.executeQuery(sql)) {
                while (r.next()) {
                    System.out.println("Looked up 'nobody' and got: " + r.getString(1) + " " + r.getBigDecimal(2));
                }
            }
        }
    }
}

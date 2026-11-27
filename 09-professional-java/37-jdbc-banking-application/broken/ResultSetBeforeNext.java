import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** A ResultSet starts BEFORE the first row. You must call next() before reading. */
public class ResultSetBeforeNext {

    public static void main(String[] args) throws SQLException {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo2;create=true"); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE account (owner VARCHAR(20))");
            s.executeUpdate("INSERT INTO account VALUES ('Asha')");
            try (ResultSet rows = s.executeQuery("SELECT owner FROM account")) {
                System.out.println(rows.getString(1));
            } catch (SQLException e) {
                System.out.println("SQLState " + e.getSQLState() + ": " + e.getMessage());
            }
        }
    }
}

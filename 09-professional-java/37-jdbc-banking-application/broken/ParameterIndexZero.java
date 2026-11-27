import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/** JDBC counts parameters from 1, not from 0 like arrays do. */
public class ParameterIndexZero {

    public static void main(String[] args) throws SQLException {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo3;create=true"); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE account (owner VARCHAR(20))");
        }
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:demo3");
             PreparedStatement p = c.prepareStatement("INSERT INTO account (owner) VALUES (?)")) {
            p.setString(0, "Asha");
        } catch (SQLException e) {
            System.out.println("SQLState " + e.getSQLState() + ": " + e.getMessage());
        }
    }
}

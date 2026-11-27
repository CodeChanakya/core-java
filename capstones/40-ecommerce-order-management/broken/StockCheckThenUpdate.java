import java.sql.*;

/** BROKEN: check the stock in one statement, take it in another. Two buyers, one item. */
public class StockCheckThenUpdate {
    public static void main(String[] args) throws Exception {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
        try (Connection setup = DriverManager.getConnection("jdbc:derby:memory:broken1;create=true");
             Statement s = setup.createStatement()) {
            s.executeUpdate("CREATE TABLE product (sku VARCHAR(10) PRIMARY KEY, stock INT NOT NULL)");
            s.executeUpdate("INSERT INTO product VALUES ('EL-1', 1)");
        }
        // Buyer A and buyer B both look first ...
        int seenByA = stock();
        int seenByB = stock();
        System.out.println("Buyer A sees stock " + seenByA + ", buyer B sees stock " + seenByB);
        // ... and then both take one, because each saw 1.
        if (seenByA >= 1) take("A");
        if (seenByB >= 1) take("B");
        System.out.println("Stock is now " + stock() + " (it should never be below 0)");
    }

    static int stock() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:broken1");
             ResultSet r = c.createStatement().executeQuery("SELECT stock FROM product WHERE sku = 'EL-1'")) {
            r.next();
            return r.getInt(1);
        }
    }

    static void take(String buyer) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:derby:memory:broken1")) {
            c.createStatement().executeUpdate("UPDATE product SET stock = stock - 1 WHERE sku = 'EL-1'");
            System.out.println("Buyer " + buyer + " got the item");
        }
    }
}

package com.codechanakya.shop;

import java.math.BigDecimal;
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

/**
 * The Derby version of {@link ShopStore}. All SQL in the shop is in this one class.
 *
 * <p>Stock is taken with {@code UPDATE product SET stock = stock - ? WHERE sku = ? AND stock >= ?}. The
 * check and the change are one statement, so two customers cannot both buy the last item. If the statement
 * changes no row, the stock was not there, and the whole order is rolled back.
 */
public class JdbcShopStore implements ShopStore {

    private final ConnectionProvider provider;

    public JdbcShopStore(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void addProduct(Product p) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO product (sku, name, category, price, stock) VALUES (?, ?, ?, ?, ?)")) {
            s.setString(1, p.sku());
            s.setString(2, p.name());
            s.setString(3, p.category().name());
            s.setBigDecimal(4, p.price());
            s.setInt(5, p.stock());
            s.executeUpdate();
        } catch (SQLException e) {
            throw translate("Adding a product", e);
        }
    }

    @Override
    public Optional<Product> findProduct(String sku) {
        return queryProducts("SELECT sku, name, category, price, stock FROM product WHERE sku = ?", sku).stream().findFirst();
    }

    @Override
    public List<Product> products() {
        return queryProducts("SELECT sku, name, category, price, stock FROM product ORDER BY sku");
    }

    @Override
    public Customer addCustomer(String name, String city) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO customer (name, city) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.setString(2, city);
            s.executeUpdate();
            try (ResultSet keys = s.getGeneratedKeys()) {
                keys.next();
                return new Customer(keys.getInt(1), name, city);
            }
        } catch (SQLException e) {
            throw translate("Adding a customer", e);
        }
    }

    @Override
    public Optional<Customer> findCustomer(int id) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "SELECT id, name, city FROM customer WHERE id = ?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? Optional.of(new Customer(r.getInt(1), r.getString(2), r.getString(3))) : Optional.empty();
            }
        } catch (SQLException e) {
            throw translate("Finding a customer", e);
        }
    }

    @Override
    public Order placeOrder(int customerId, List<OrderLine> lines, BigDecimal discount, LocalDate date) {
        try (Connection c = provider.open()) {
            c.setAutoCommit(false);
            try {
                for (OrderLine line : lines) {
                    try (PreparedStatement s = c.prepareStatement(
                            "UPDATE product SET stock = stock - ? WHERE sku = ? AND stock >= ?")) {
                        s.setInt(1, line.quantity());
                        s.setString(2, line.sku());
                        s.setInt(3, line.quantity());
                        if (s.executeUpdate() == 0) {
                            throw new OutOfStockException(line.sku(), line.quantity());
                        }
                    }
                }
                BigDecimal subtotal = lines.stream().map(OrderLine::total).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal total = subtotal.subtract(discount);
                int id;
                try (PreparedStatement s = c.prepareStatement(
                        "INSERT INTO orders (customer_id, status, discount, total, placed_on) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    s.setInt(1, customerId);
                    s.setString(2, OrderStatus.NEW.name());
                    s.setBigDecimal(3, discount);
                    s.setBigDecimal(4, total);
                    s.setDate(5, Date.valueOf(date));
                    s.executeUpdate();
                    try (ResultSet keys = s.getGeneratedKeys()) {
                        keys.next();
                        id = keys.getInt(1);
                    }
                }
                for (OrderLine line : lines) {
                    try (PreparedStatement s = c.prepareStatement(
                            "INSERT INTO order_line (order_id, sku, quantity, unit_price) VALUES (?, ?, ?, ?)")) {
                        s.setInt(1, id);
                        s.setString(2, line.sku());
                        s.setInt(3, line.quantity());
                        s.setBigDecimal(4, line.unitPrice());
                        s.executeUpdate();
                    }
                }
                c.commit();
                return new Order(id, customerId, OrderStatus.NEW, lines, discount, total, date);
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                if (e instanceof SQLException sql) {
                    throw translate("Placing an order", sql);
                }
                throw (RuntimeException) e;
            }
        } catch (SQLException e) {
            throw translate("Placing an order", e);
        }
    }

    @Override
    public Optional<Order> findOrder(int id) {
        return queryOrders("SELECT id, customer_id, status, discount, total, placed_on FROM orders WHERE id = ?", id)
                .stream().findFirst();
    }

    @Override
    public List<Order> orders() {
        return queryOrders("SELECT id, customer_id, status, discount, total, placed_on FROM orders ORDER BY id");
    }

    @Override
    public void updateStatus(int orderId, OrderStatus status) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(
                "UPDATE orders SET status = ? WHERE id = ?")) {
            s.setString(1, status.name());
            s.setInt(2, orderId);
            s.executeUpdate();
        } catch (SQLException e) {
            throw translate("Updating an order", e);
        }
    }

    @Override
    public void cancelAndRestock(int orderId) {
        Order order = findOrder(orderId).orElseThrow(() -> new ShopException("No order with id " + orderId));
        try (Connection c = provider.open()) {
            c.setAutoCommit(false);
            try {
                for (OrderLine line : order.lines()) {
                    try (PreparedStatement s = c.prepareStatement("UPDATE product SET stock = stock + ? WHERE sku = ?")) {
                        s.setInt(1, line.quantity());
                        s.setString(2, line.sku());
                        s.executeUpdate();
                    }
                }
                try (PreparedStatement s = c.prepareStatement("UPDATE orders SET status = ? WHERE id = ?")) {
                    s.setString(1, OrderStatus.CANCELLED.name());
                    s.setInt(2, orderId);
                    s.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw translate("Cancelling an order", e);
            }
        } catch (SQLException e) {
            throw translate("Cancelling an order", e);
        }
    }

    private List<Product> queryProducts(String sql, Object... params) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, params);
            try (ResultSet r = s.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (r.next()) {
                    list.add(new Product(r.getString(1), r.getString(2), Category.valueOf(r.getString(3)),
                            r.getBigDecimal(4), r.getInt(5)));
                }
                return list;
            }
        } catch (SQLException e) {
            throw translate("Reading products", e);
        }
    }

    private List<Order> queryOrders(String sql, Object... params) {
        try (Connection c = provider.open(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, params);
            List<Order> list = new ArrayList<>();
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    list.add(new Order(r.getInt(1), r.getInt(2), OrderStatus.valueOf(r.getString(3)), List.of(),
                            r.getBigDecimal(4), r.getBigDecimal(5), r.getDate(6).toLocalDate()));
                }
            }
            List<Order> full = new ArrayList<>();
            for (Order o : list) {
                full.add(new Order(o.id(), o.customerId(), o.status(), linesOf(c, o.id()), o.discount(), o.total(), o.placedOn()));
            }
            return full;
        } catch (SQLException e) {
            throw translate("Reading orders", e);
        }
    }

    private List<OrderLine> linesOf(Connection c, int orderId) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(
                "SELECT sku, quantity, unit_price FROM order_line WHERE order_id = ? ORDER BY sku")) {
            s.setInt(1, orderId);
            try (ResultSet r = s.executeQuery()) {
                List<OrderLine> lines = new ArrayList<>();
                while (r.next()) {
                    lines.add(new OrderLine(r.getString(1), r.getInt(2), r.getBigDecimal(3)));
                }
                return lines;
            }
        }
    }

    private static void bind(PreparedStatement s, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            s.setObject(i + 1, params[i]);
        }
    }

    /** 23505 is a duplicate key, 23513 a broken CHECK rule. */
    private static ShopException translate(String action, SQLException e) {
        if ("23505".equals(e.getSQLState())) {
            return new ShopException(action + " failed: that already exists");
        }
        return new ShopException(action + " failed (SQLState " + e.getSQLState() + "): " + e.getMessage(), e);
    }
}

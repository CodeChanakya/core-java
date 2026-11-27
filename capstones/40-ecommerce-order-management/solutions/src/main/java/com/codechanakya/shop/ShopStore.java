package com.codechanakya.shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Everything the shop needs from its database. */
public interface ShopStore {

    void addProduct(Product product);

    Optional<Product> findProduct(String sku);

    /** All products ordered by sku. */
    List<Product> products();

    Customer addCustomer(String name, String city);

    Optional<Customer> findCustomer(int id);

    /**
     * Saves a new order and takes the stock, in one transaction.
     *
     * @throws OutOfStockException when any product does not have enough stock; nothing is saved
     */
    Order placeOrder(int customerId, List<OrderLine> lines, BigDecimal discount, LocalDate date);

    Optional<Order> findOrder(int id);

    /** All orders ordered by id. */
    List<Order> orders();

    void updateStatus(int orderId, OrderStatus status);

    /** Cancels an order: marks it CANCELLED and gives the stock back, in one transaction. */
    void cancelAndRestock(int orderId);
}

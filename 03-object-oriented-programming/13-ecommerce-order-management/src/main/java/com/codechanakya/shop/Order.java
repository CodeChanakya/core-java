package com.codechanakya.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * A customer's order: who bought, what they bought, and where it stands.
 *
 * Relationships used here:
 *   Order -> Customer    association  (the customer outlives the order)
 *   Order -> OrderItem   composition  (the items belong to this order alone)
 *   OrderItem -> Product association  (products live on in the catalog)
 */
public class Order {

    // Plain text statuses for now. Program 14 replaces them with an enum, which
    // is safer because a typo such as "PAYED" would then fail to compile.
    public static final String NEW = "NEW";
    public static final String PAID = "PAID";
    public static final String SHIPPED = "SHIPPED";

    private static final BigDecimal GST_RATE = new BigDecimal("0.18");

    private final String id;
    private final Customer customer;
    private final ArrayList<OrderItem> items = new ArrayList<>();
    private String status = NEW;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO.setScale(2);
        for (OrderItem item : items) {
            sum = sum.add(item.lineTotal());
        }
        return sum;
    }

    public BigDecimal discount() {
        return subtotal().multiply(customer.discountRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal shipping() {
        BigDecimal sum = BigDecimal.ZERO.setScale(2);
        for (OrderItem item : items) {
            sum = sum.add(item.shipping());
        }
        return sum;
    }

    /** GST is charged on the goods after the discount, not on shipping. */
    public BigDecimal gst() {
        return subtotal().subtract(discount()).multiply(GST_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total() {
        return subtotal().subtract(discount()).add(shipping()).add(gst());
    }

    /** NEW to PAID. Returns false if the order is not waiting for payment. */
    public boolean markPaid() {
        if (!status.equals(NEW)) {
            return false;
        }
        status = PAID;
        return true;
    }

    /** PAID to SHIPPED. An unpaid order must not leave the warehouse. */
    public boolean markShipped() {
        if (!status.equals(PAID)) {
            return false;
        }
        status = SHIPPED;
        return true;
    }

    public String invoice() {
        StringBuilder text = new StringBuilder();
        text.append("Order ").append(id).append(" for ").append(customer.getName());
        text.append(customer.isLoyaltyMember() ? " (loyalty member)" : "").append('\n');
        for (OrderItem item : items) {
            text.append(String.format("  %-22s %-8s %2d x %9s = %10s%n",
                    item.getProduct().getName(), item.getProduct().kind(), item.getQuantity(),
                    item.getProduct().getPrice().toPlainString(), item.lineTotal().toPlainString()));
        }
        text.append(String.format("  %-45s %10s%n", "Subtotal", subtotal().toPlainString()));
        text.append(String.format("  %-45s %10s%n", "Loyalty discount", discountText()));
        text.append(String.format("  %-45s %10s%n", "Shipping", shipping().toPlainString()));
        text.append(String.format("  %-45s %10s%n", "GST 18%", gst().toPlainString()));
        text.append(String.format("  %-45s %10s%n", "TOTAL", total().toPlainString()));
        text.append("  Status: ").append(status);
        return text.toString();
    }

    /** A zero discount prints as 0.00, not -0.00. */
    private String discountText() {
        return discount().signum() == 0 ? "0.00" : "-" + discount().toPlainString();
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getStatus() {
        return status;
    }

    public int itemCount() {
        return items.size();
    }
}

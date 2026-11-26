package com.codechanakya.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A cart backed by an ArrayList.
 *
 * A List is the right choice here because the order items were added matters
 * to the shopper, the same product can be looked at by position, and the cart
 * stays small enough that scanning it is never a problem.
 */
public class ShoppingCart {

    /** Alphabetical by product name. */
    public static final Comparator<CartItem> BY_NAME =
            Comparator.comparing(CartItem::getName);

    /** Biggest spend first. */
    public static final Comparator<CartItem> BY_LINE_TOTAL_DESC =
            Comparator.comparing(CartItem::lineTotal).reversed();

    /** Most copies first, and by name when the quantities tie. */
    public static final Comparator<CartItem> BY_QUANTITY_THEN_NAME =
            Comparator.comparingInt(CartItem::getQuantity).reversed()
                    .thenComparing(CartItem::getName);

    /** I1: cheapest unit price first. */
    public static final Comparator<CartItem> BY_UNIT_PRICE_ASC =
            Comparator.comparing(CartItem::getUnitPrice);

    private final List<CartItem> items = new ArrayList<>();

    /** Adds a product, or tops up the existing line when the SKU is already in the cart. */
    public void add(String sku, String name, String price, int quantity) {
        for (CartItem existing : items) {
            if (existing.getSku().equals(sku)) {
                existing.addQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(sku, name, new BigDecimal(price), quantity));
    }

    /** Removes the line with this SKU. Returns false when there was no such line. */
    public boolean remove(String sku) {
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getSku().equals(sku)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Drops every line worth less than the given amount, returning how many went.
     * An Iterator is the safe way to delete while walking a list.
     */
    public int removeLinesBelow(BigDecimal minimum) {
        int removed = 0;
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().lineTotal().compareTo(minimum) < 0) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    /** A sorted copy. The cart itself keeps its original order. */
    public List<CartItem> sortedBy(Comparator<CartItem> order) {
        List<CartItem> copy = new ArrayList<>(items);
        copy.sort(order);
        return copy;
    }

    /** A read-only window onto the cart. Callers can look, but not change it. */
    public List<CartItem> view() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal total() {
        BigDecimal sum = BigDecimal.ZERO.setScale(2);
        for (CartItem item : items) {
            sum = sum.add(item.lineTotal());
        }
        return sum;
    }

    public int lineCount() {
        return items.size();
    }

    /** Items as a plain array, for code that still expects one. */
    public CartItem[] toArray() {
        return items.toArray(new CartItem[0]);
    }

    public void print(String heading) {
        System.out.println(heading);
        for (CartItem item : items) {
            System.out.println("  " + item);
        }
        System.out.println("  Lines: " + items.size() + ", total " + total());
    }

    /** B2: is there a line with this SKU? */
    public boolean contains(String sku) {
        for (CartItem item : items) {
            if (item.getSku().equals(sku)) {
                return true;
            }
        }
        return false;
    }

    /** I2: set a line's quantity. Zero or less removes the line. Returns false for an unknown SKU. */
    public boolean updateQuantity(String sku, int quantity) {
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            CartItem item = it.next();
            if (item.getSku().equals(sku)) {
                if (quantity <= 0) {
                    it.remove();
                } else {
                    item.setQuantity(quantity);
                }
                return true;
            }
        }
        return false;
    }

    /** C1: adds every line of another cart into this one, merging SKUs. The other cart is untouched. */
    public void mergeFrom(ShoppingCart other) {
        for (CartItem item : other.view()) {
            add(item.getSku(), item.getName(), item.getUnitPrice().toPlainString(), item.getQuantity());
        }
    }
}

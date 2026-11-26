package com.codechanakya.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.add("P-101", "USB-C Cable", "299.00", 2);
        cart.add("P-205", "Notebook", "85.50", 6);
        cart.add("P-310", "Desk Lamp", "1250.00", 1);
        cart.add("P-101", "USB-C Cable", "299.00", 1);   // same SKU: merges into the first line
        cart.add("P-412", "Pen Pack", "40.00", 3);
        cart.add("P-520", "Headphones", "1999.00", 1);

        cart.print("--- The cart, in the order things were added ---");

        System.out.println();
        System.out.println("--- Sorted copies (the cart itself is not reordered) ---");
        show("By name", cart.sortedBy(ShoppingCart.BY_NAME));
        show("By line total, biggest first", cart.sortedBy(ShoppingCart.BY_LINE_TOTAL_DESC));
        show("By quantity, then name", cart.sortedBy(ShoppingCart.BY_QUANTITY_THEN_NAME));
        System.out.println("Still first in the cart: " + cart.view().get(0).getName());

        System.out.println();
        System.out.println("--- Removing with an Iterator ---");
        System.out.println("Remove P-310: " + cart.remove("P-310"));
        System.out.println("Remove P-999: " + cart.remove("P-999"));
        int gone = cart.removeLinesBelow(new BigDecimal("150.00"));
        System.out.println("Lines worth less than 150.00 removed: " + gone);
        cart.print("After clean-up");

        System.out.println();
        System.out.println("--- What goes wrong ---");
        List<String> names = new ArrayList<>(List.of("Asha", "Rahul", "Meera", "Kiran"));
        try {
            for (String name : names) {
                if (name.startsWith("A")) {
                    names.remove(name);          // changes the list under the loop's feet
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Removing inside a for-each: " + e.getClass().getSimpleName());
        }
        try {
            cart.view().add(new CartItem("X-1", "Sneaky", BigDecimal.ONE, 1));
        } catch (UnsupportedOperationException e) {
            System.out.println("Adding to the read-only view: " + e.getClass().getSimpleName());
        }
        try {
            List.of(1, 2, 3).add(4);
        } catch (UnsupportedOperationException e) {
            System.out.println("Adding to List.of(...): " + e.getClass().getSimpleName());
        }
        try {
            cart.view().get(10);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("get(10) on a short list: " + e.getMessage());
        }
    }

    private static void show(String heading, List<CartItem> items) {
        System.out.println(heading + ":");
        for (CartItem item : items) {
            System.out.println("  " + item);
        }
    }
}

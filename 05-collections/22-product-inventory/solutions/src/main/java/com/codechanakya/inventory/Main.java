package com.codechanakya.inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.add(new Product("E-100", "USB-C Cable", Category.ELECTRONICS, "299.00", 40, 10));
        inventory.add(new Product("E-110", "Headphones", Category.ELECTRONICS, "1999.00", 6, 8));
        inventory.add(new Product("S-200", "Notebook", Category.STATIONERY, "85.50", 120, 30));
        inventory.add(new Product("S-210", "Pen Pack", Category.STATIONERY, "40.00", 25, 25));
        inventory.add(new Product("H-300", "Desk Lamp", Category.HOME, "1250.00", 3, 5));
        inventory.add(new Product("S-220", "Pencil Box", Category.STATIONERY, "120.00", 18, 10));    // B1

        System.out.println("--- Stock by category ---");
        for (Map.Entry<Category, List<Product>> group : inventory.byCategory().entrySet()) {
            System.out.println(group.getKey() + ":");
            for (Product product : group.getValue()) {
                System.out.println("  " + product);
            }
        }

        System.out.println();
        System.out.println("--- Value of stock per category ---");
        for (Map.Entry<Category, BigDecimal> entry : inventory.valueByCategory().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println();
        System.out.println("--- Reorder list, emptiest first ---");
        for (Product product : inventory.reorderList()) {
            System.out.println("  " + product.getName() + ": " + product.getStock()
                    + " left, reorder at " + product.getReorderLevel());
        }
        System.out.println("  Categories affected: " + inventory.categoriesToReorder());

        System.out.println();
        System.out.println("--- Selling and receiving ---");
        try {
            inventory.sell("E-100", 15);
            inventory.sell("S-200", 100);
            inventory.receive("H-300", 12);
            System.out.println("  Three movements recorded.");
            inventory.sell("E-110", 10);
        } catch (OutOfStockException e) {
            System.out.println("  Refused: " + e.getMessage());
            System.out.println("  Short by: " + e.getShortfall());
        } catch (UnknownProductException e) {
            System.out.println("  Unexpected: " + e.getMessage());
        }
        try {
            inventory.sell("X-999", 1);
        } catch (UnknownProductException | OutOfStockException e) {
            System.out.println("  Refused: " + e.getMessage());
        }

        System.out.println();
        System.out.println("--- Movement history, newest first ---");
        for (StockMovement movement : inventory.recentMovements()) {
            System.out.println("  " + movement);
        }

        System.out.println();
        System.out.println("--- Undo the last two movements ---");
        for (int i = 0; i < 2; i++) {
            System.out.println("  Undid: " + inventory.undoLast());
        }
        System.out.println("  Notebook stock is back to " + inventory.find("S-200").getStock());
        System.out.println("  Lamp stock is back to     " + inventory.find("H-300").getStock());

        System.out.println();
        System.out.println("--- Reorder list now ---");
        for (Product product : inventory.reorderList()) {
            System.out.println("  " + product.getName() + ": " + product.getStock() + " left");
        }

        System.out.println();
        System.out.println("--- Exercise answers ---");
        System.out.println("  Total units (B2): " + inventory.totalUnits());
        for (Map.Entry<Category, List<Product>> group : inventory.byCategory().entrySet()) {
            System.out.println("  " + group.getKey() + ": " + group.getValue().size() + " products");       // B3
        }
        System.out.println("  Search \"pen\" (I1): " + names(inventory.search("pen")));
        try {
            inventory.sell("E-100", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("  Refused (I2): " + e.getMessage());
        } catch (UnknownProductException | OutOfStockException e) {
            System.out.println("  Unexpected: " + e.getMessage());
        }
        // C1: undo, then bring it back.
        try {
            inventory.sell("E-100", 5);
            System.out.println("  Cable stock after selling 5: " + inventory.find("E-100").getStock());
            inventory.undoLast();
            System.out.println("  After undo                 : " + inventory.find("E-100").getStock());
            inventory.redoLast();
            System.out.println("  After redo                 : " + inventory.find("E-100").getStock());
        } catch (UnknownProductException | OutOfStockException e) {
            System.out.println("  Unexpected: " + e.getMessage());
        }
    }

    private static String names(List<Product> products) {
        StringBuilder text = new StringBuilder();
        for (Product product : products) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(product.getName());
        }
        return text.toString();
    }
}

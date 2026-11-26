package com.codechanakya.inventory;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;

/**
 * The shop's stock, using one collection for each job it suits.
 *
 *  - Map<String, Product>      find a product by SKU quickly, keep the order they were added.
 *  - Map<Category, List<...>>  group products for reports.
 *  - Deque<StockMovement>      a stack of changes, so the latest can be undone.
 *  - Set<Category>             "which families have something to reorder?" with no repeats.
 */
public class Inventory {

    private static final Comparator<Product> LOWEST_STOCK_FIRST =
            Comparator.comparingInt(Product::getStock).thenComparing(Product::getName);

    private final Map<String, Product> products = new LinkedHashMap<>();
    private final Deque<StockMovement> movements = new ArrayDeque<>();
    private final Deque<StockMovement> undone = new ArrayDeque<>();      // C1: what redo can bring back

    public void add(Product product) {
        products.put(product.getSku(), product);
    }

    private Product require(String sku) throws UnknownProductException {
        Product product = products.get(sku);
        if (product == null) {
            throw new UnknownProductException(sku);
        }
        return product;
    }

    public void receive(String sku, int units) throws UnknownProductException {
        if (units <= 0) {
            throw new IllegalArgumentException("Units received must be positive, got " + units);    // I2
        }
        Product product = require(sku);
        product.changeStock(units);
        movements.push(new StockMovement(sku, units, "received"));
        undone.clear();                          // a new action makes the old redo history meaningless
    }

    public void sell(String sku, int units) throws UnknownProductException, OutOfStockException {
        if (units <= 0) {
            throw new IllegalArgumentException("Units sold must be positive, got " + units);        // I2
        }
        Product product = require(sku);
        if (units > product.getStock()) {
            throw new OutOfStockException(product.getName(), units, product.getStock());
        }
        product.changeStock(-units);
        movements.push(new StockMovement(sku, -units, "sold"));
        undone.clear();
    }

    /** Reverses the latest movement. Returns it, or null when there is nothing to undo. */
    public StockMovement undoLast() {
        StockMovement last = movements.poll();
        if (last != null) {
            products.get(last.getSku()).changeStock(-last.getChange());
            undone.push(last);
        }
        return last;
    }

    /** C1: applies the most recently undone movement again. Returns null when there is nothing to redo. */
    public StockMovement redoLast() {
        StockMovement again = undone.poll();
        if (again != null) {
            products.get(again.getSku()).changeStock(again.getChange());
            movements.push(again);
        }
        return again;
    }

    /** Products grouped by category, categories in their declared order. */
    public Map<Category, List<Product>> byCategory() {
        Map<Category, List<Product>> groups = new EnumMap<>(Category.class);
        for (Product product : products.values()) {
            MapUtil.addToGroup(groups, product.getCategory(), product);
        }
        return groups;
    }

    /** Money tied up in stock for each category. */
    public Map<Category, BigDecimal> valueByCategory() {
        Map<Category, BigDecimal> totals = new EnumMap<>(Category.class);
        for (Product product : products.values()) {
            totals.merge(product.getCategory(), product.stockValue(), BigDecimal::add);
        }
        return totals;
    }

    /** Products at or below their reorder level, emptiest first. */
    public List<Product> reorderList() {
        List<Product> low = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.needsReorder()) {
                low.add(product);
            }
        }
        low.sort(LOWEST_STOCK_FIRST);
        return low;
    }

    /** Categories that have at least one product to reorder, with no repeats. */
    public Set<Category> categoriesToReorder() {
        Set<Category> categories = new TreeSet<>();
        for (Product product : reorderList()) {
            categories.add(product.getCategory());
        }
        return categories;
    }

    public List<StockMovement> recentMovements() {
        return Collections.unmodifiableList(new ArrayList<>(movements));
    }

    public Product find(String sku) {
        return products.get(sku);
    }

    /** B2: all units on all shelves. */
    public int totalUnits() {
        int total = 0;
        for (Product product : products.values()) {
            total += product.getStock();
        }
        return total;
    }

    /** I1: products whose name contains the text, ignoring case, sorted by name. */
    public List<Product> search(String text) {
        String wanted = text.toLowerCase();
        List<Product> found = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getName().toLowerCase().contains(wanted)) {
                found.add(product);
            }
        }
        found.sort(Comparator.comparing(Product::getName));
        return found;
    }
}

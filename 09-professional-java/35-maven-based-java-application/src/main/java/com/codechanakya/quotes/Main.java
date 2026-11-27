package com.codechanakya.quotes;

import java.util.List;

/** Loads the catalog from the classpath and prints four quotes. */
public class Main {

    public static void main(String[] args) {
        Catalog catalog = CatalogLoader.fromResource("/catalog.csv");
        System.out.println("Catalog loaded from the classpath: " + catalog.size() + " products");
        QuoteCalculator calculator = new QuoteCalculator(catalog);

        print("A", calculator, List.of(new QuoteItem("P-100", 2), new QuoteItem("P-200", 3)));
        print("B", calculator, List.of(new QuoteItem("E-200", 1), new QuoteItem("E-300", 1), new QuoteItem("E-100", 2)));
        print("C", calculator, List.of(new QuoteItem("F-100", 1), new QuoteItem("P-100", 1)));
        print("D", calculator, List.of(new QuoteItem("E-100", 1), new QuoteItem("X-999", 1)));
    }

    private static void print(String label, QuoteCalculator calculator, List<QuoteItem> items) {
        System.out.println();
        System.out.println("Quote " + label);
        try {
            Quote quote = calculator.price(items);
            for (QuoteLine line : quote.lines()) {
                System.out.printf("  %-15s x%d  net %9s  tax %8s  gross %9s%n", line.product().name(), line.quantity(),
                        line.net().toPlainString(), line.tax().toPlainString(), line.gross().toPlainString());
            }
            System.out.println("  Subtotal : " + quote.subtotal().toPlainString());
            System.out.println("  Discount : " + quote.discount().toPlainString()
                    + " (" + quote.discountRate().movePointRight(2).stripTrailingZeros().toPlainString() + "%)");
            System.out.println("  Tax      : " + quote.tax().toPlainString());
            System.out.println("  Total    : " + quote.total().toPlainString());
        } catch (UnknownProductException e) {
            System.out.println("  Rejected: " + e.getMessage());
        }
    }
}

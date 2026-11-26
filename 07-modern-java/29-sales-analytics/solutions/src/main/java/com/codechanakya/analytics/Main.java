package com.codechanakya.analytics;

import java.math.BigDecimal;
import java.util.IntSummaryStatistics;
import java.util.Locale;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Analytics analytics = new Analytics(SalesData.sample());

        System.out.println("--- Total per bucket ---");
        System.out.println("  Revenue by region    : " + analytics.revenueByRegion());
        System.out.println("  Units by region      : " + analytics.unitsByRegion());
        System.out.println("  Orders by category   : " + analytics.ordersByCategory());

        System.out.println();
        System.out.println("--- Averages and shares ---");
        for (Map.Entry<String, Double> entry : analytics.averageUnitsByRegion().entrySet()) {
            // Locale.ROOT, so the decimal point is a point on every machine.
            System.out.println("  " + entry.getKey() + " averages " + String.format(Locale.ROOT, "%.1f", entry.getValue()) + " units per order");
        }
        for (Map.Entry<String, BigDecimal> entry : analytics.shareByRegion().entrySet()) {
            System.out.println("  " + entry.getKey() + " is " + entry.getValue() + " percent of revenue");
        }

        System.out.println();
        System.out.println("--- What is inside each bucket ---");
        System.out.println("  Salespeople by region: " + analytics.peopleByRegion());
        System.out.println("  Biggest sale by region: " + analytics.biggestSaleByRegion());

        System.out.println();
        System.out.println("--- Ranking salespeople ---");
        for (Map.Entry<String, BigDecimal> entry : analytics.topSalespeople(3)) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("  Everyone (toMap with a merge rule): " + analytics.revenueBySalesperson());

        System.out.println();
        System.out.println("--- Splitting in two ---");
        Map<Boolean, Long> split = analytics.splitByOrderValue(new BigDecimal("5000"));
        System.out.println("  Orders of 5000 or more: " + split.get(true) + ", smaller: " + split.get(false));

        System.out.println();
        System.out.println("--- Several answers in one pass ---");
        System.out.println("  teeing: " + analytics.unitsAndOrders());
        IntSummaryStatistics stats = analytics.unitStatistics();
        System.out.println("  Units per order: min " + stats.getMin() + ", max " + stats.getMax()
                + ", sum " + stats.getSum() + ", count " + stats.getCount());

        System.out.println();
        System.out.println("--- Group, then flatten ---");
        System.out.println("  " + analytics.orderIdsGroupedThenFlattened());
        System.out.println("  Regions: " + analytics.regionNamesJoined());

        System.out.println();
        System.out.println("--- Exercise answers ---");
        System.out.println("  West revenue by filtering (B1): " + SalesData.sample().stream()
                .filter(sale -> sale.region().equals("West")).map(Sale::revenue).reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("  Revenue by category (B2): " + analytics.revenueByCategory());
        System.out.println("  Different salespeople (B3): " + SalesData.sample().stream().map(Sale::salesperson).distinct().count());
        System.out.println("  Orders per salesperson (I1): " + analytics.ordersBySalesperson());
        System.out.println("  Electronics or not (I2): " + analytics.electronicsOrNot());
        System.out.println("  Top category per region (C1): " + analytics.topCategoryByRegion());
    }
}

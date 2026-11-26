package com.codechanakya.analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Reports over a list of sales, each built from one Collector.
 *
 * Collectors.groupingBy(f) means "put each item in the bucket named by f(item)", and a second
 * argument says what to do with each bucket's items. Every map here is created with a TreeMap
 * (or EnumMap) so the output order is fixed. A plain HashMap would work, but its order is not
 * promised and could differ between Java versions.
 */
public class Analytics {

    private final List<Sale> sales;

    public Analytics(List<Sale> sales) {
        this.sales = sales;
    }

    /** Region to total revenue. reducing() folds each bucket's items into one value. */
    public Map<String, BigDecimal> revenueByRegion() {
        return sales.stream().collect(Collectors.groupingBy(Sale::region, TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, Sale::revenue, BigDecimal::add)));
    }

    /** Category to number of orders. counting() gives a Long. */
    public Map<Category, Long> ordersByCategory() {
        return sales.stream().collect(Collectors.groupingBy(Sale::category,
                () -> new EnumMap<>(Category.class), Collectors.counting()));
    }

    /** Region to total units. */
    public Map<String, Integer> unitsByRegion() {
        return sales.stream().collect(Collectors.groupingBy(Sale::region, TreeMap::new,
                Collectors.summingInt(Sale::units)));
    }

    /** Region to the average number of units per order. */
    public Map<String, Double> averageUnitsByRegion() {
        return sales.stream().collect(Collectors.groupingBy(Sale::region, TreeMap::new,
                Collectors.averagingInt(Sale::units)));
    }

    /** Region to the distinct salespeople who sold there, in alphabetical order. */
    public Map<String, TreeSet<String>> peopleByRegion() {
        return sales.stream().collect(Collectors.groupingBy(Sale::region, TreeMap::new,
                Collectors.mapping(Sale::salesperson, Collectors.toCollection(TreeSet::new))));
    }

    /** Region to the order id of its single biggest sale. maxBy gives an Optional; collectingAndThen opens it. */
    public Map<String, String> biggestSaleByRegion() {
        return sales.stream().collect(Collectors.groupingBy(Sale::region, TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(Sale::revenue)),
                        best -> best.map(Sale::orderId).orElse("none"))));
    }

    /** Salesperson to total revenue. toMap needs a rule for two items with the same key: here, add them. */
    public Map<String, BigDecimal> revenueBySalesperson() {
        return sales.stream().collect(Collectors.toMap(Sale::salesperson, Sale::revenue,
                BigDecimal::add, TreeMap::new));
    }

    /** The n best salespeople by revenue, best first, ties broken by name. */
    public List<Map.Entry<String, BigDecimal>> topSalespeople(int n) {
        return revenueBySalesperson().entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .toList();
    }

    /** True: orders worth at least the limit. False: the rest. partitioningBy always has both keys. */
    public Map<Boolean, Long> splitByOrderValue(BigDecimal limit) {
        return sales.stream().collect(Collectors.partitioningBy(
                sale -> sale.revenue().compareTo(limit) >= 0, Collectors.counting()));
    }

    /** Each region's share of all revenue, as a percentage with one decimal place. */
    public Map<String, BigDecimal> shareByRegion() {
        BigDecimal total = revenueByRegion().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, BigDecimal> shares = new TreeMap<>();
        revenueByRegion().forEach((region, revenue) ->
                shares.put(region, revenue.multiply(BigDecimal.valueOf(100)).divide(total, 1, RoundingMode.HALF_UP)));
        return shares;
    }

    /** Count and sum in one pass over the data, using teeing to run two collectors at once. */
    public String unitsAndOrders() {
        return sales.stream().collect(Collectors.teeing(
                Collectors.summingInt(Sale::units),
                Collectors.counting(),
                (units, orders) -> units + " units in " + orders + " orders"));
    }

    /** min, max, average, sum and count of the unit numbers, from one call. */
    public IntSummaryStatistics unitStatistics() {
        return sales.stream().collect(Collectors.summarizingInt(Sale::units));
    }

    /** Grouped lists flattened back into one sorted stream of order ids. */
    public List<String> orderIdsGroupedThenFlattened() {
        Map<String, List<Sale>> byRegion = sales.stream()
                .collect(Collectors.groupingBy(Sale::region, TreeMap::new, Collectors.toList()));
        return byRegion.values().stream()
                .flatMap(List::stream)            // one stream from many lists
                .map(Sale::orderId)
                .sorted()
                .toList();
    }

    /** Names joined into one string: the simplest collector. */
    public String regionNamesJoined() {
        return sales.stream().map(Sale::region).distinct().sorted().collect(Collectors.joining(", ", "[", "]"));
    }
}

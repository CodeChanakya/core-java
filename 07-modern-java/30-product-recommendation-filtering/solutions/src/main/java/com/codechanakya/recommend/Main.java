package com.codechanakya.recommend;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Product> catalogue = Catalogue.sample();
        Recommender recommender = new Recommender(catalogue);

        System.out.println("--- Rules are values ---");
        Rule cheapAndGood = new Rule.AllOf(List.of(
                new Rule.MaxPrice(new BigDecimal("500")), new Rule.MinRating(4.0)));
        Rule notGrocery = new Rule.Not(new Rule.OfKind(Grocery.class));
        System.out.println("  Rule : " + RuleEngine.describe(cheapAndGood));
        System.out.println("  Match: " + names(recommender.filter(cheapAndGood)));
        System.out.println("  Rule : " + RuleEngine.describe(notGrocery));
        System.out.println("  Match: " + names(recommender.filter(notGrocery)));
        System.out.println("  Raw record: " + cheapAndGood);

        System.out.println();
        System.out.println("--- One switch, many shapes ---");
        for (Product product : catalogue.subList(0, 3)) {
            System.out.println("  " + product.name() + ": " + Recommender.highlight(product));
        }
        System.out.println("  " + catalogue.get(4).name() + ": " + Recommender.highlight(catalogue.get(4)));
        System.out.println("  " + catalogue.get(8).name() + ": " + Recommender.highlight(catalogue.get(8)));
        System.out.println("  " + catalogue.get(9).name() + ": " + Recommender.highlight(catalogue.get(9)));

        System.out.println();
        System.out.println("--- Recommendations ---");
        Shopper developer = new Shopper("Developer on a budget", new BigDecimal("2000"), 4.3,
                List.of(Book.class, Gadget.class), false);
        show(recommender, developer);
        Shopper vegetarian = new Shopper("Vegetarian shopper", new BigDecimal("700"), 4.0,
                List.of(Grocery.class, Book.class), true);
        show(recommender, vegetarian);
        Shopper picky = new Shopper("Very picky", new BigDecimal("100"), 4.9, List.of(Book.class), false);
        show(recommender, picky);

        System.out.println();
        System.out.println("--- Pattern matching on a single value ---");
        Object[] things = {catalogue.get(0), "a string", 42, new Rule.MinRating(3.5), List.of(1, 2)};
        for (Object thing : things) {
            System.out.println("  " + classify(thing));
        }

        System.out.println();
        System.out.println("--- Exercise answers ---");
        for (Product product : catalogue) {                                              // B2
            System.out.println("  " + product.name() + ": " + Recommender.highlight(product));
        }
        Shopper bigger = new Shopper("Vegetarian with a bigger budget", new BigDecimal("1000"), 4.0,   // B3
                List.of(Grocery.class, Book.class), true);
        show(recommender, bigger);
        Rule longWarranty = new Rule.MinWarranty(24);                                    // I1
        System.out.println("  " + RuleEngine.describe(longWarranty) + ": " + names(recommender.filter(longWarranty)));
        Shopper parent = new Shopper("Parent of a toddler", new BigDecimal("1000"), 4.0, List.of(Toy.class), false);   // I2
        show(recommender, parent);
        Rule doubled = new Rule.Not(new Rule.Not(new Rule.MinRating(4.5)));              // C1
        System.out.println("  Before: " + doubled);
        System.out.println("  After : " + RuleEngine.simplify(doubled));

        System.out.println();
        System.out.println("--- Counting the catalogue by kind ---");
        Map<String, Long> byKind = catalogue.stream().collect(
                Collectors.groupingBy(p -> p.getClass().getSimpleName(), TreeMap::new, Collectors.counting()));
        System.out.println("  " + byKind);
    }

    private static void show(Recommender recommender, Shopper shopper) {
        System.out.println("  " + shopper.name() + ":");
        System.out.println("    wants: " + RuleEngine.describe(shopper.toRule()));
        List<Recommendation> picks = recommender.recommend(shopper, 3);
        if (picks.isEmpty()) {
            System.out.println("    nothing matches");
        }
        for (Recommendation pick : picks) {
            System.out.println("    " + String.format(Locale.ROOT, "%.1f", pick.score()) + "  "
                    + pick.product().name() + " - " + pick.note());
        }
        System.out.println("    best pick: " + recommender.bestPick(shopper).map(r -> r.product().name()).orElse("none"));
    }

    /** A pattern-matching switch over any Object. Without sealing, the compiler requires a default. */
    private static String classify(Object thing) {
        return switch (thing) {
            case Book(String name, var price, var rating, String author, int pages) -> "a book called " + name + " by " + author;
            case Product p -> "some other product: " + p.name();
            case String s when s.length() > 5 -> "a long string (" + s.length() + " characters)";
            case String s -> "a short string";
            case Integer i -> "the integer " + i;
            case Rule r -> "a rule: " + RuleEngine.describe(r);
            default -> "something else: " + thing;
        };
    }

    private static String names(List<Product> products) {
        return products.stream().map(Product::name).collect(Collectors.joining(", "));
    }
}

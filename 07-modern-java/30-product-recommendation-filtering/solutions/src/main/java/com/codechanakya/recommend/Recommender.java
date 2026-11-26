package com.codechanakya.recommend;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Filters a catalogue by rules, scores what is left, and explains each choice. */
public class Recommender {

    private final List<Product> catalogue;

    public Recommender(List<Product> catalogue) {
        this.catalogue = catalogue;
    }

    /** The shopper's best matches, highest score first, ties broken by name. */
    public List<Recommendation> recommend(Shopper shopper, int limit) {
        Rule rule = shopper.toRule();
        return catalogue.stream()
                .filter(product -> RuleEngine.matches(rule, product))
                .map(product -> new Recommendation(product, score(product), highlight(product)))
                .sorted(Comparator.comparingDouble(Recommendation::score).reversed()
                        .thenComparing(r -> r.product().name()))
                .limit(limit)
                .toList();
    }

    /** The single best match, if anything matched at all. */
    public Optional<Recommendation> bestPick(Shopper shopper) {
        return recommend(shopper, 1).stream().findFirst();
    }

    /** Products that match a rule, in catalogue order. */
    public List<Product> filter(Rule rule) {
        return catalogue.stream().filter(product -> RuleEngine.matches(rule, product)).toList();
    }

    /** Rating counts most; each kind of product adds its own small bonus or penalty. */
    static double score(Product product) {
        double base = product.rating() * 10;
        return switch (product) {
            case Book b -> base + (b.pages() > 300 ? 2 : 0);
            case Gadget g -> base + g.warrantyMonths() / 12.0;
            case Grocery g -> base - (g.shelfLifeDays() <= 3 ? 5 : 0);
            case Toy t -> base + (t.minAge() <= 5 ? 1 : 0);
        };
    }

    /** One sentence about a product. "when" adds a condition to a case, and the first match wins. */
    static String highlight(Product product) {
        return switch (product) {
            case Book b when b.pages() > 500 -> "a long read by " + b.author();
            case Book b -> "a quick read by " + b.author();
            case Gadget g when g.wireless() -> "wireless, with " + g.warrantyMonths() + " months of warranty";
            case Gadget g -> "wired, with " + g.warrantyMonths() + " months of warranty";
            case Grocery(String name, var price, var rating, boolean veg, int days) when days <= 7 ->
                    (veg ? "vegetarian" : "contains meat") + ", fresh: use within " + days + " days";
            case Grocery g -> (g.vegetarian() ? "vegetarian" : "contains meat") + ", keeps for " + g.shelfLifeDays() + " days";
            case Toy t -> "for ages " + t.minAge() + " and up";
        };
    }
}

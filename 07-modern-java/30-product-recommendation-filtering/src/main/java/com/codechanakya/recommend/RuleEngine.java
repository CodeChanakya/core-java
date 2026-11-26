package com.codechanakya.recommend;

import java.util.Locale;
import java.util.stream.Collectors;

/** Reads a Rule and answers questions about it. All the work is pattern-matching switches. */
public final class RuleEngine {

    private RuleEngine() {
    }

    /**
     * Does this product satisfy the rule?
     *
     * There is no "default" branch. Rule is sealed, so the compiler knows the seven cases below are
     * all of them, and adding an eighth rule type will stop this method compiling until it is handled.
     */
    public static boolean matches(Rule rule, Product product) {
        return switch (rule) {
            case Rule.MinRating r -> product.rating() >= r.value();
            case Rule.MaxPrice r -> product.price().compareTo(r.value()) <= 0;
            case Rule.OfKind r -> r.kind().isInstance(product);
            case Rule.VegetarianOnly r -> !(product instanceof Grocery g) || g.vegetarian();
            case Rule.Not(Rule inner) -> !matches(inner, product);                       // a record pattern
            case Rule.AllOf(var rules) -> rules.stream().allMatch(r -> matches(r, product));
            case Rule.AnyOf(var rules) -> rules.stream().anyMatch(r -> matches(r, product));
        };
    }

    /** A sentence describing the rule, built the same way. */
    public static String describe(Rule rule) {
        return switch (rule) {
            case Rule.MinRating r -> "rated at least " + String.format(Locale.ROOT, "%.1f", r.value());
            case Rule.MaxPrice r -> "costs no more than " + r.value();
            case Rule.OfKind r -> "is a " + r.kind().getSimpleName().toLowerCase();
            case Rule.VegetarianOnly r -> "is vegetarian when it is food";
            case Rule.Not(Rule inner) -> "is not (" + describe(inner) + ")";
            case Rule.AllOf(var rules) -> rules.stream().map(RuleEngine::describe).collect(Collectors.joining(" and ", "(", ")"));
            case Rule.AnyOf(var rules) -> rules.stream().map(RuleEngine::describe).collect(Collectors.joining(" or ", "(", ")"));
        };
    }
}

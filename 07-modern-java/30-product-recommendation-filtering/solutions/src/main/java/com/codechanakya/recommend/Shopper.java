package com.codechanakya.recommend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** What we know about the person we are recommending for. */
public record Shopper(String name, BigDecimal budget, double minRating,
                      List<Class<? extends Product>> interests, boolean vegetarian) {

    /** Turns the shopper's wishes into one rule. */
    public Rule toRule() {
        // <Rule> tells Java the list holds Rules, not just OfKind, so it can be passed to AnyOf.
        List<Rule> interestRules = interests.stream().<Rule>map(Rule.OfKind::new).toList();
        List<Rule> parts = new ArrayList<>();
        parts.add(new Rule.MinRating(minRating));
        parts.add(new Rule.MaxPrice(budget));
        parts.add(new Rule.AnyOf(interestRules));
        if (vegetarian) {
            parts.add(new Rule.VegetarianOnly());
        }
        return new Rule.AllOf(parts);
    }
}

package com.codechanakya.recommend;

import java.math.BigDecimal;
import java.util.List;

/**
 * A condition a product must meet, written as data rather than as code.
 *
 * Because rules are values, they can be combined (AllOf, AnyOf, Not), printed, stored and tested.
 * The sealed list means RuleEngine's switches can be checked for completeness by the compiler.
 */
public sealed interface Rule permits Rule.MinRating, Rule.MaxPrice, Rule.OfKind, Rule.VegetarianOnly,
        Rule.Not, Rule.AllOf, Rule.AnyOf, Rule.MinWarranty {

    record MinRating(double value) implements Rule {
    }

    record MaxPrice(BigDecimal value) implements Rule {
    }

    record OfKind(Class<? extends Product> kind) implements Rule {
    }

    record VegetarianOnly() implements Rule {
    }

    record Not(Rule inner) implements Rule {
    }

    record AllOf(List<Rule> rules) implements Rule {
    }

    record AnyOf(List<Rule> rules) implements Rule {
    }

    /** I1: a gadget with at least this much warranty. Everything that is not a gadget fails it. */
    record MinWarranty(int months) implements Rule {
    }
}

# 30 · Product Recommendation Filtering

**Phase 07 · Modern Java** &nbsp;|&nbsp; **Level:** Advanced · phase project &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: sealed interfaces, records, pattern-matching `switch`, record patterns, `when` guards, `Optional`, streams.

---

## The problem

An online shop sells books, gadgets and groceries. Each shopper has a budget, a minimum rating, the kinds of product they want and perhaps a dietary rule. The shop wants a list of recommendations, in a sensible order, with a reason for each. The filtering rules must be easy to combine and easy to print, and when a new kind of product is added, the compiler should point out every place that needs updating.

This is the **phase project**. It uses lambdas and streams (27), records and `Optional` (28), collectors (29), and adds sealed types and pattern matching.

## Requirements

**Functional**
1. Three kinds of product with their own details.
2. Rules as values: a minimum rating, a maximum price, a kind, vegetarian only, and `Not`, `AllOf` and `AnyOf` to combine them.
3. Check a rule against a product, and describe a rule in words.
4. Turn a shopper's wishes into one rule, score the matches and sort them.
5. Give each recommendation a one-line highlight that depends on the kind.
6. Return the best pick as an `Optional`.

**Non-functional**
- Adding a product kind or a rule kind must force every `switch` to be updated, at compile time.
- No rule or product can be built in an invalid state.
- Output is deterministic.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`Product.java`](src/main/java/com/codechanakya/recommend/Product.java) and the three records | `sealed interface ... permits`. |
| 2 | [`Rule.java`](src/main/java/com/codechanakya/recommend/Rule.java) | A sealed interface with records inside it. |
| 3 | [`RuleEngine.java`](src/main/java/com/codechanakya/recommend/RuleEngine.java) | Exhaustive switches with record patterns and no `default`. |
| 4 | [`Shopper.java`](src/main/java/com/codechanakya/recommend/Shopper.java) | Wishes become one `Rule`. |
| 5 | [`Recommender.java`](src/main/java/com/codechanakya/recommend/Recommender.java) | Score, sort, highlight, best pick. |
| 6 | [`Main.java`](src/main/java/com/codechanakya/recommend/Main.java) | Five sections, ending with a pattern switch on `Object`. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Run it from this folder (`run.sh` does that for you). Nothing here reads or writes files, so there is nothing to clean up.

Expected output:

```text
--- Rules are values ---
  Rule : (costs no more than 500 and rated at least 4.0)
  Match: USB-C Cable, Fresh Paneer 200g, Chicken Sausages
  Rule : is not (is a grocery)
  Match: The Pragmatic Programmer, Clean Code, Java Concurrency in Practice, A Pocket Guide to SQL, Noise-Cancelling Headphones, USB-C Cable, Mechanical Keyboard
  Raw record: AllOf[rules=[MaxPrice[value=500], MinRating[value=4.0]]]

--- One switch, many shapes ---
  The Pragmatic Programmer: a quick read by Hunt and Thomas
  Clean Code: a quick read by Robert Martin
  Java Concurrency in Practice: a long read by Brian Goetz
  Noise-Cancelling Headphones: wireless, with 24 months of warranty
  Fresh Paneer 200g: vegetarian, fresh: use within 3 days
  Chicken Sausages: contains meat, fresh: use within 5 days

--- Recommendations ---
  Developer on a budget:
    wants: (rated at least 4.3 and costs no more than 2000 and (is a book or is a gadget))
    50.0  The Pragmatic Programmer - a quick read by Hunt and Thomas
    49.0  Java Concurrency in Practice - a long read by Brian Goetz
    47.0  Noise-Cancelling Headphones - wireless, with 24 months of warranty
    best pick: The Pragmatic Programmer
  Vegetarian shopper:
    wants: (rated at least 4.0 and costs no more than 700 and (is a grocery or is a book) and is vegetarian when it is food)
    43.0  Basmati Rice 5kg - vegetarian, keeps for 365 days
    36.0  Fresh Paneer 200g - vegetarian, fresh: use within 3 days
    best pick: Basmati Rice 5kg
  Very picky:
    wants: (rated at least 4.9 and costs no more than 100 and (is a book))
    nothing matches
    best pick: none

--- Pattern matching on a single value ---
  a book called The Pragmatic Programmer by Hunt and Thomas
  a long string (8 characters)
  the integer 42
  a rule: rated at least 3.5
  something else: [1, 2]

--- Counting the catalogue by kind ---
  {Book=4, Gadget=3, Grocery=3}
```

## Sealed types: a closed list of possibilities

```java
public sealed interface Product permits Book, Gadget, Grocery { ... }
```

`sealed` says that **only the listed types** may implement this interface. Anyone else who tries gets a compile error ([`broken/`](broken/README.md)). Ordinary inheritance is open: anybody can add a subclass later, so code that handles "all the kinds" can never be sure it has seen them all. A sealed hierarchy is closed, so the compiler knows the complete list. Each permitted type must be `final`, `sealed` or `non-sealed`; records are implicitly final.

## Pattern matching in switch

```java
return switch (product) {
    case Book b    -> "a " + (b.pages() > 300 ? "long" : "quick") + " read by " + b.author();
    case Gadget g  -> ...
    case Grocery g -> ...
};
```

`case Book b` tests the type **and** names the value in one step, with no cast. Because `Product` is sealed and every permitted type has a case, the switch is **exhaustive**, and so it needs no `default`. That is the useful part: add a fourth kind of product and every switch like this stops compiling until you decide what the new kind should do. A `default` would hide that. Use one only when "everything else" really is one behaviour, as in `classify(Object)`.

## Record patterns

```java
case Rule.Not(Rule inner)   -> !matches(inner, product);
case Rule.AllOf(var rules)  -> rules.stream().allMatch(r -> matches(r, product));
```

A record pattern takes a record apart in the `case` itself, naming its components. It matches only that record, and the names are available on the right of the arrow. It can nest, and a record can only be taken apart like this because its shape is public.

## Guards with `when`

```java
case Book b when b.pages() > 300 -> ...
case Book b                      -> ...
```

`when` adds a condition to a pattern. Order matters: Java checks cases from the top, so the specific one must come first. If a general case comes before a specific one, the specific one can never run, and the compiler reports it as **dominated** ([`broken/`](broken/README.md)).

## Rules as data

A `Rule` is not a method but a small *tree* of records: `AllOf([MaxPrice(500), MinRating(4.0)])`. Because it is data, the same tree can be **evaluated** (`matches`), **printed** (`describe`), compared and stored. The first section of the output prints one rule three ways. A lambda could evaluate but could not describe itself. This is the real trade-off: lambdas are shorter, data is inspectable.

## Streams, collectors and Optional, together

`Recommender.recommend` filters with the rule, maps each match to a scored `Recommendation`, sorts by score and returns a list. `bestPick` returns the first as an `Optional`, so "nothing matches" for *Very picky* prints `none` without a `null` anywhere. The last section counts the catalogue with `groupingBy`.

## Common mistakes

| Mistake | What you see |
|---|---|
| Missing case in a switch over a sealed type | compile error: the switch expression does not cover all possible input values |
| Adding `default` to be "safe" | the compiler stops checking, and a new type is silently handled wrongly |
| A subclass not named in `permits` | compile error: class is not allowed to extend sealed class |
| A general `case` above a specific one | compile error: this case label is dominated by a preceding case label |
| Casting after `instanceof` by hand | works, but pattern matching does both at once |

## Practice

- [`exercises/`](exercises/README.md): six tasks.
- [`solutions/`](solutions/README.md): one answer to each.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Phase 8 is Concurrency: running work at the same time and keeping the data correct. Records and immutable values from this phase make that much easier.

# Exercises: Product Recommendation Filtering

Work in a copy so the original stays intact:

```bash
cp -r 30-product-recommendation-filtering ../my-product-recommendation-filtering
cd ../my-product-recommendation-filtering
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a fourth grocery item to the catalogue and check that it appears when a shopper asks for groceries.

<details><summary>Hint</summary>Copy one `new Grocery(...)` line and change the values.</details>

**B2.** Print `Recommender.highlight` for every product in the catalogue.

<details><summary>Hint</summary>Loop over the catalogue and call `highlight(product)`.</details>

**B3.** Create a vegetarian shopper with a bigger budget and a different set of kinds.

<details><summary>Hint</summary>`new Shopper(name, budget, minRating, kinds, vegetarian)`.</details>

## Intermediate

**I1.** Add a rule `MinWarranty(int months)`. Make the compiler show you every switch that needs a case.

<details><summary>Hint 1</summary>Add the record to `Rule` and to its `permits` list. Compile.</details>
<details><summary>Hint 2</summary>Add a case to `matches` and to `describe`. Anything that is not a `Gadget` should fail the rule.</details>

**I2.** Add a fourth product kind, `Toy`, with a minimum age. Let the compiler guide you to the switches.

<details><summary>Hint 1</summary>Create the record, then add it to `permits`.</details>
<details><summary>Hint 2</summary>Every exhaustive switch over `Product` will now fail to compile until it has a `Toy` case.</details>

## Challenge

**C1.** Write `simplify(Rule)` that turns `Not(Not(x))` into `x`, everywhere inside a rule tree.

<details><summary>Hint 1</summary>A nested record pattern: `case Rule.Not(Rule.Not(Rule inner))`.</details>
<details><summary>Hint 2</summary>Recurse into `AllOf` and `AnyOf`. This switch needs a `default`; think about what that costs you.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

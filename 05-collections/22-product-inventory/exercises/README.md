# Exercises: Product Inventory

Work in a copy so the original stays intact:

```bash
cp -r 22-product-inventory ../my-product-inventory
cd ../my-product-inventory
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a sixth product in STATIONERY and look at the reports again.

<details><summary>Hint</summary>`inventory.add(new Product(...))` with a new SKU.</details>

**B2.** Add `totalUnits()` to `Inventory`.

<details><summary>Hint</summary>Loop over `products.values()` and add up the stock.</details>

**B3.** Print how many products each category has.

<details><summary>Hint</summary>Use the map from `byCategory()`; each value is a list, and a list has a `size()`.</details>

## Intermediate

**I1.** Add `search(String text)` returning products whose name contains the text, ignoring case, sorted by name.

<details><summary>Hint</summary>Compare `toLowerCase()` of both sides with `contains`.</details>

**I2.** Make `sell` and `receive` refuse zero or negative units with an unchecked exception.

<details><summary>Hint</summary>Should this be checked or unchecked? Program 15 gave the rule: a caller's mistake is unchecked.</details>

## Challenge

**C1.** Add `redoLast()`, which re-applies an undone movement. A new sale or receipt must clear the redo history.

<details><summary>Hint 1</summary>A second `Deque` holds what was undone.</details>
<details><summary>Hint 2</summary>`undoLast` pushes onto it; `redoLast` pops from it and applies the change again.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

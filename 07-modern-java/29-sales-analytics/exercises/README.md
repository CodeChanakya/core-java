# Exercises: Sales Analytics

Work in a copy so the original stays intact:

```bash
cp -r 29-sales-analytics ../my-sales-analytics
cd ../my-sales-analytics
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Work out the West region revenue with `filter` and `reduce`, and check it against the grouped answer.

<details><summary>Hint</summary>`reduce(BigDecimal.ZERO, BigDecimal::add)` after mapping to `Sale::revenue`.</details>

**B2.** Add `revenueByCategory()` returning an `EnumMap`.

<details><summary>Hint 1</summary>Use the supplier `() -> new EnumMap<>(Category.class)`.</details>
<details><summary>Hint 2</summary>`reducing(BigDecimal.ZERO, Sale::revenue, BigDecimal::add)` is the downstream collector.</details>

**B3.** Count the different salespeople.

<details><summary>Hint</summary>`map(Sale::salesperson).distinct().count()`.</details>

## Intermediate

**I1.** Add `ordersBySalesperson()` that counts orders per person, sorted by name.

<details><summary>Hint</summary>`groupingBy(Sale::salesperson, TreeMap::new, counting())`.</details>

**I2.** Split the order ids into electronics and not electronics.

<details><summary>Hint 1</summary>`partitioningBy` takes a second argument, a downstream collector.</details>
<details><summary>Hint 2</summary>`mapping(Sale::orderId, toList())`.</details>

## Challenge

**C1.** For each region find the category that earned the most.

<details><summary>Hint 1</summary>Group by region, and inside it group by category with a revenue total.</details>
<details><summary>Hint 2</summary>`collectingAndThen` turns that inner map into its best key with `max(Map.Entry.comparingByValue())`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

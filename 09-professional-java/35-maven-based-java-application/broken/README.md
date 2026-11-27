# Break it on purpose

Each file here shows one mistake that matters in a Maven project. They use only the JDK, so no Maven is needed to run them.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ResourceLookup.java`: a resource name without the leading slash

```bash
mkdir -p out
javac -d out ResourceLookup.java
printf 'sku\n' > out/catalog.csv
java -cp out demo.ResourceLookup
```

```text
/catalog.csv  -> found
catalog.csv   -> null (not found)
```

The file sits at the root of the classpath, like `catalog.csv` in `target/classes`. The name with `/` finds it. The name without `/` is looked up inside the class's own package, which here is the folder `demo/`, and the answer is `null`. `CatalogLoader` guards against that: when the stream is `null` it throws a message that tells you to check `src/main/resources`.

## 2. `MoneyAsDouble.java`: money in a double

```bash
javac MoneyAsDouble.java
java MoneyAsDouble
```

```text
double : 3 x 0.10 = 0.30000000000000004
double : equals 0.30? false
decimal: 3 x 0.10 = 0.30
decimal: equals 0.30? true
```

`double` stores binary fractions, and 0.10 has none. Three times 0.10 is not 0.30. A total with this tiny error fails every equality check, and the error grows as amounts are added. The catalog and the calculator use `BigDecimal` made from text such as `"0.10"` for that reason.

## 3. `SplitDropsEmptyCells.java`: split(",") drops empty cells at the end

```bash
javac SplitDropsEmptyCells.java
java SplitDropsEmptyCells
```

```text
split(",")     -> 2 cells
split(",", -1) -> 4 cells
```

A line with two missing values at its end looks like a line with two columns, so a loader that counts cells reports the wrong problem. `split(",", -1)` keeps every cell, and `CatalogLoader` uses it.

*The output above was captured from a real run with OpenJDK 21.0.13. Other versions may word some messages a little differently.*

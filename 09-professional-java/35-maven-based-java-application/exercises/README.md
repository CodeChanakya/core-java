# Exercises: Maven-based Java Application

Work in a copy so the original stays intact:

```bash
cp -r 35-maven-based-java-application ../my-maven-based-java-application
cd ../my-maven-based-java-application
./run.sh
```

Edit the files under `src` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**A1.** Add a new product, `P-300`, a Stapler at 85.00 with 5 percent tax, and print a quote for four staplers.

<details><summary>Hint</summary>Only `catalog.csv` needs a new line. No Java changes.</details>

**A2.** Add a third discount tier: 15 percent off a subtotal of 3000 or more.

<details><summary>Hint</summary>Add a constant next to the other two and one more `if` at the top of `discountRateFor`, because the highest tier must be checked first.</details>

**A3.** Open `pom.xml` and change `<version>` to `1.1.0`. Which file name would `mvn package` now produce?

<details><summary>Hint</summary>The jar name is `artifactId-version.jar`.</details>

## Intermediate

**B1.** Add `Catalog.byPrice()` returning the products from cheapest to dearest, and print them in `Main`.

<details><summary>Hint</summary>`Comparator.comparing(Product::unitPrice)`, then `thenComparing(Product::sku)` to break ties.</details>

**B2.** Refuse a quantity above 100 in `QuoteItem`, with a message that names the SKU.

<details><summary>Hint</summary>Put the check in the compact constructor of the record, next to the existing one.</details>

## Challenge

**C1.** With Maven installed, run `mvn -q -B package`, then `jar tf target/quote-calculator-1.0.0.jar`. Find `catalog.csv` in the list. What does that tell you about where resources end up?

<details><summary>Hint</summary>It sits at the root of the jar, next to the `com` folder.</details>

**C2.** Use Maven resource filtering to put the project version into the output. Create `src/main/resources/app.properties` containing `version=${{project.version}}`, switch filtering on in `pom.xml` with a `<resources>` block, and print the value from `Main`. Build with Maven and then with `run.sh`. What difference do you see, and why?

<details><summary>Hint 1</summary>Add `<build><resources><resource><directory>src/main/resources</directory><filtering>true</filtering></resource></resources></build>`.</details>
<details><summary>Hint 2</summary>`run.sh` only copies files, so it never replaces `${{project.version}}`. Only Maven does that.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src). Exercises C1 and C2 need Maven, so they have no code in the solutions.

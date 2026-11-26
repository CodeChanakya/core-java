# Exercises: Customer Search

Work in a copy so the original stays intact:

```bash
cp -r 19-customer-search ../my-customer-search
cd ../my-customer-search
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Sign up a customer in Mumbai and print the cities again.

<details><summary>Hint</summary>`directory.signUp(new Customer(...))`, then `directory.cities()`.</details>

**B2.** Add `cityCount()` to `CustomerDirectory`.

<details><summary>Hint</summary>A set knows its own `size()`.</details>

**B3.** Print all customer names from Z to A.

<details><summary>Hint</summary>Put the names in a `TreeSet<String>` and call `descendingSet()`.</details>

## Intermediate

**I1.** Add `customersSortedByCity()` returning unique customers ordered by city, then by name.

<details><summary>Hint 1</summary>`Comparator.comparing(Customer::getCity).thenComparing(Customer::getName)`.</details>
<details><summary>Hint 2</summary>Start from `firstSignUps()` and call `sort`.</details>

**I2.** Add `emailDomains()` returning the part after the `@` of every email, sorted, with no repeats.

<details><summary>Hint</summary>`email.substring(email.indexOf('@') + 1)`.</details>

## Challenge

**C1.** Add `namesBetween(String from, String to)` returning the names from one to another, inclusive, ignoring case.

<details><summary>Hint 1</summary>A `TreeSet` can be given a comparator: `new TreeSet<>(String.CASE_INSENSITIVE_ORDER)`.</details>
<details><summary>Hint 2</summary>`subSet(from, true, to, true)` returns the range.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

# Exercises: Customer Data Processing

Work in a copy so the original stays intact:

```bash
cp -r 27-customer-data-processing ../my-customer-data-processing
cd ../my-customer-data-processing
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Print the names of customers older than 40.

<details><summary>Hint</summary>`filter(c -> c.getAge() > 40)`, then `map(Customer::getName)`.</details>

**B2.** Print the email of every inactive customer, using `forEach`.

<details><summary>Hint</summary>`filter(c -> !c.isActive())`, then `map(Customer::getEmail)`, then `forEach`.</details>

**B3.** Count the customers who live in Pune.

<details><summary>Hint</summary>`filter` on the city and finish with `count()`.</details>

## Intermediate

**I1.** Write `inCity(String city)` that returns a `Predicate<Customer>`, and use it with `and` to list active customers in Mumbai.

<details><summary>Hint 1</summary>The method returns a lambda: `return customer -> ...;`.</details>
<details><summary>Hint 2</summary>The parameter is captured, so it must not be reassigned.</details>

**I2.** Write a `Discount` that never lets an amount go above a limit, and chain it after the ten percent discount.

<details><summary>Hint 1</summary>`amount -> amount.min(max)`.</details>
<details><summary>Hint 2</summary>`tenPercent.then(cap)` uses the default method.</details>

## Challenge

**C1.** Write a generic method `select(List<T>, Predicate<T>)` that works for customers and for strings.

<details><summary>Hint 1</summary>Declare `<T>` before the return type.</details>
<details><summary>Hint 2</summary>A new `ArrayList<T>`, a loop, and `test.test(item)`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

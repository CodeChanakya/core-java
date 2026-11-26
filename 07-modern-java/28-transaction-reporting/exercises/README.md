# Exercises: Transaction Reporting

Work in a copy so the original stays intact:

```bash
cp -r 28-transaction-reporting ../my-transaction-reporting
cd ../my-transaction-reporting
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add one more valid line to the sample text and check the loaded count.

<details><summary>Hint</summary>Add a line with five comma-separated fields in the correct format.</details>

**B2.** Add `smallestWithdrawal()` to `Ledger`, returning an `Optional`.

<details><summary>Hint</summary>`filter` on the kind, then `min(Comparator.comparing(Transaction::amount))`.</details>

**B3.** Print the oldest transaction.

<details><summary>Hint</summary>`Transaction` has a natural order by date. Use `min(Comparator.naturalOrder())`.</details>

## Intermediate

**I1.** Reject ids that do not start with `T-`.

<details><summary>Hint</summary>Add the check to the compact constructor before the amount check.</details>

**I2.** Add `summaryFor(String account)` returning `Optional<AccountSummary>`, and print it for ACC-2 and ACC-9.

<details><summary>Hint</summary>Stream `summaries()` and use `findFirst()`.</details>

## Challenge

**C1.** Create a `Money` record (amount and a three-letter currency) with `plus`, that refuses to add different currencies.

<details><summary>Hint 1</summary>The compact constructor checks the currency length.</details>
<details><summary>Hint 2</summary>`plus` compares `currency.equals(other.currency)` first.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

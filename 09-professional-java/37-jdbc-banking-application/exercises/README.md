# Exercises: JDBC Banking Application

Work in a copy so the original stays intact:

```bash
cp -r 37-jdbc-banking-application ../my-jdbc-banking-application
cd ../my-jdbc-banking-application
./run.sh
```

Edit the files under `src` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**A1.** Add `findByMinBalance(Connection, BigDecimal)` to `AccountRepository`: accounts holding at least that much, richest first. Print the accounts with 500.00 or more.

<details><summary>Hint</summary>`WHERE balance >= ? ORDER BY balance DESC, id` and `setBigDecimal(1, minimum)`.</details>

**A2.** Open a sixth account for a new customer with 75.50. Print the id the database chose.

<details><summary>Hint</summary>`open` already returns an `Account` with the generated id.</details>

**A3.** Change the URL in `Database.java` from `memory:bank` to `memory:other`. Does anything change? What would change if you made two `Database` objects with different names?

<details><summary>Hint</summary>Each name is a separate database. Tables made in one are invisible to the other.</details>

## Intermediate

**B1.** Add `closeAccount(int id)` to `TransferService`. It deletes the account, but only when the balance is exactly zero; otherwise it refuses with a message that shows the balance. Use a transaction.

<details><summary>Hint</summary>Read the account, check, delete, commit. Roll back on any exception, as `transfer` does.</details>

**B2.** Add a search for accounts whose owner contains a given text, case-insensitive, using a placeholder.

<details><summary>Hint</summary>`WHERE LOWER(owner) LIKE ?` with `"%" + text.toLowerCase(Locale.ROOT) + "%"` as the parameter. The `%` signs belong to the value, not to the SQL.</details>

## Challenge

**C1.** Add `transferAll(List<Move>)` that runs several transfers in one transaction. If any leg fails, none of them happens. Test it with a second leg that fails.

<details><summary>Hint 1</summary>Keep the loop inside one `setAutoCommit(false)` block, with one `commit()` at the end and one `rollback()` in the catch.</details>
<details><summary>Hint 2</summary>Check the balances of the first payer afterwards: they must be unchanged.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src). Run the solution version with `./solutions/run.sh` (after `./run.sh` has downloaded the driver once).

# Exercises: Banking Application

Work in a copy so the original stays intact:

```bash
cp -r 08-banking-application ../my-banking-application
cd ../my-banking-application
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Open a fourth account in `Main` for yourself and look at the new statement.

<details><summary>Hint</summary>`bank.openAccount(...)` does everything. The number is chosen for you.</details>

**B2.** Add `isEmpty()` to `Account`. It returns `true` when the balance is zero. Use it in `Main` to say which accounts are empty.

<details><summary>Hint</summary>`balance.signum()` returns 0 for zero, 1 for positive and -1 for negative.</details>

**B3.** In `Main`, print "Asha Menon has 4750.25" using the getters, not `toString`.

<details><summary>Hint</summary>`getHolderName()` and `getBalance().toPlainString()`.</details>

## Intermediate

**I1.** Add `richestAccount()` to `Bank`. It returns the account with the largest balance, or `null` for an empty bank.

<details><summary>Hint 1</summary>Remember the best account so far, starting with `null`.</details>
<details><summary>Hint 2</summary>Compare balances with `compareTo`, never `>`.</details>

**I2.** Add a limit: one account may withdraw at most 20000.00 in total. A withdrawal that would pass the limit is refused.

<details><summary>Hint 1</summary>You need a new field that remembers how much has been withdrawn so far.</details>
<details><summary>Hint 2</summary>Add the new withdrawal to it, compare with the limit, and only then change the balance.</details>

## Challenge

**C1.** Give each account a history. Every successful deposit and withdrawal adds a line such as `DEPOSIT 750.25`, and `printHistory()` prints them in order.

<details><summary>Hint 1</summary>Use an `ArrayList<String>` field, like the list inside `Bank`.</details>
<details><summary>Hint 2</summary>Add to the list only after the balance has changed, so refused operations leave no trace.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

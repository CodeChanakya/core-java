# Exercises: Banking Exception Management

Work in a copy so the original stays intact:

```bash
cp -r 15-banking-exception-management ../my-banking-exception-management
cd ../my-banking-exception-management
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Run a deposit of 0 and read the message. Where in the code is that message built?

<details><summary>Hint</summary>Follow the exception from `Main` back to `requirePositive` in `Account`.</details>

**B2.** Add a `finally` block to scenario 1 that prints "Statement printed".

<details><summary>Hint</summary>A `finally` goes after the last `catch`.</details>

**B3.** Add "(short by X)" to the message of `InsufficientFundsException`.

<details><summary>Hint</summary>The constructor already has both numbers; subtract them.</details>

## Intermediate

**I1.** Add a checked `LimitExceededException` thrown when one withdrawal is above 50000.00. What else must change so the program still compiles?

<details><summary>Hint 1</summary>Add it to the `throws` of `withdraw`.</details>
<details><summary>Hint 2</summary>Every caller now has to catch it or declare it. The compiler lists them for you.</details>

**I2.** Add `Bank.safeBalance(String)` that returns the balance as text, or "unknown account", without letting an exception escape.

<details><summary>Hint</summary>A `try` around `find`, and a `catch` that returns the fallback.</details>

## Challenge

**C1.** Add `batchTransfer(from, String[] to, each)` that pays several accounts. If any payment fails, pay back the ones already made.

<details><summary>Hint 1</summary>Count successful payments as you go.</details>
<details><summary>Hint 2</summary>In the `catch`, loop over the first `completed` payments and reverse each, then rethrow.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

# Exercises: JUnit Starter Lab

Work in a copy so the original stays intact:

```bash
cp -r 09-junit-starter-lab ../my-junit-starter-lab
cd ../my-junit-starter-lab
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Write a test that the holder name is trimmed: an account created with `"  Meera Iyer  "` reports `"Meera Iyer"`.

<details><summary>Hint</summary>Create the account inside the test, not in `@BeforeEach`; it needs different input.</details>

**B2.** Write a test that the smallest possible deposit, 0.01, is accepted and changes the balance.

<details><summary>Hint</summary>`assertTrue` on the result and `assertEquals` on the new balance.</details>

**B3.** Write a test that `toString()` mentions the account number.

<details><summary>Hint</summary>`assertTrue(account.toString().contains("ACC-1"))`.</details>

## Intermediate

**I1.** Test-first: write a test for a method `isEmpty()` that does not exist yet. Watch it fail to compile, then add the method and watch it pass.

<details><summary>Hint</summary>A zero-balance account is empty; the 1000.00 account in `@BeforeEach` is not.</details>

**I2.** Write a test that a refused withdrawal of a negative amount leaves the balance untouched.

<details><summary>Hint</summary>Two assertions: the method returned `false`, and the balance is still `1000.00`.</details>

## Challenge

**C1.** Test-first again: add `transferTo(Account other, BigDecimal amount)`. Write two tests before the method: one successful transfer, and one that cannot be paid for and must move nothing.

<details><summary>Hint 1</summary>Both balances must be checked in the failing case, not just the sender's.</details>
<details><summary>Hint 2</summary>Implementation idea: `withdraw` first; only if that works, `deposit` into the other account.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

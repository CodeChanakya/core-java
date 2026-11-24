# Exercises: Order Status Management

Work in a copy so the original stays intact:

```bash
cp -r 14-order-status-management ../my-order-status-management
cd ../my-order-status-management
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a status `REFUNDED` that may follow `RETURNED`, and make `RETURNED` no longer final. Compile before you touch `nextStates()` and read what the compiler says.

<details><summary>Hint</summary>The switch will refuse to compile until you add a case for the new constant.</details>

**B2.** Add `isFinished()` to `Order` that reports whether the status is final.

<details><summary>Hint</summary>Ask the status: `status.isFinished()`.</details>

**B3.** Print every status with its position and label using `values()`.

<details><summary>Hint</summary>A for-each loop over `OrderStatus.values()`, then `ordinal()` and `getLabel()`.</details>

## Intermediate

**I1.** Add `countFinished(Order[])` to `OrderBoard`.

<details><summary>Hint</summary>Use the method from B2.</details>

**I2.** Add a static method `OrderStatus.fromText(String)` that ignores case and spaces and returns `null` for unknown text instead of throwing.

<details><summary>Hint</summary>`text.trim().toUpperCase()`, then compare with each constant's `name()`.</details>

## Challenge

**C1.** Add an enum `ShippingSpeed` (STANDARD, EXPRESS, SAME_DAY) where every constant has its own `cost(int kilograms)` body.

<details><summary>Hint</summary>Declare `public abstract BigDecimal cost(int kilograms);` in the enum, then give each constant a `{ ... }` body that overrides it.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

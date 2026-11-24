# Exercises: Payment Processing System

Work in a copy so the original stays intact:

```bash
cp -r 12-payment-processing-system ../my-payment-processing-system
cd ../my-payment-processing-system
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Create a second `CardPayment` in `Main` with its own limit and pay with it.

<details><summary>Hint</summary>`processor.useMethod(secondCard)` switches to it.</details>

**B2.** Change the card fee from 2 percent to 1.5 percent. Predict the charged amount before running.

<details><summary>Hint</summary>`PercentageFee` takes the percent as a number, so pass 1.5.</details>

**B3.** After the payments, print how many entries are in the ledger.

<details><summary>Hint</summary>Add a small method to `PaymentProcessor` that returns the list's `size()`.</details>

## Intermediate

**I1.** Add a `NetBankingPayment` class. It must work without any change to `PaymentProcessor`.

<details><summary>Hint</summary>Implement `PaymentMethod`: `name()` and `charge(...)`.</details>

**I2.** Add a `TieredFee`: nothing under 1000, 1 percent from 1000 upwards.

<details><summary>Hint</summary>Compare the amount with `compareTo`.</details>

## Challenge

**C1.** Add a `Notifier` interface that is told about every approved payment, and give the processor one as a third part. Existing code must still work with no notifier.

<details><summary>Hint 1</summary>A `SilentNotifier` that does nothing, used by default, avoids `null` checks everywhere.</details>
<details><summary>Hint 2</summary>Add a `useNotifier` method like the other two.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

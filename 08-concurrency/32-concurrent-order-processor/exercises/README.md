# Exercises: Concurrent Order Processor

Work in a copy so the original stays intact:

```bash
cp -r 32-concurrent-order-processor ../my-concurrent-order-processor
cd ../my-concurrent-order-processor
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Run 100 customers against a stock of 100. What should happen? Check it.

<details><summary>Hint</summary>`new SynchronizedStock(100)`. All 100 should be accepted.</details>

**B2.** Run 200 customers against a stock of 60. How many are refused?

<details><summary>Hint</summary>200 minus 60.</details>

**B3.** Write `BlockStock`, the same stock using a `synchronized` block on a private lock object instead of a synchronized method.

<details><summary>Hint</summary>`private final Object lock = new Object();` and `synchronized (lock) { ... }`.</details>

## Intermediate

**I1.** Add `release(int)` to `Stock` and implement it in all three classes. Reserve 4 then release 4 and check the shelf.

<details><summary>Hint 1</summary>Adding a method to the interface makes the compiler list every class that needs it.</details>
<details><summary>Hint 2</summary>The atomic version is `units.addAndGet(quantity)`.</details>

**I2.** Make `Bank.transfer` refuse an amount of zero or less with an `IllegalArgumentException`.

<details><summary>Hint</summary>Check before taking any lock.</details>

## Challenge

**C1.** Write `tryTransfer` that avoids deadlock a different way: `tryLock` with a 10 ms timeout on both accounts, giving up (and releasing the first lock) if the second cannot be had. Run opposite transfers that keep retrying and check the total.

<details><summary>Hint 1</summary>`lock.tryLock(10, TimeUnit.MILLISECONDS)` returns a boolean.</details>
<details><summary>Hint 2</summary>Release what you hold in `finally`, and return `false` so the caller can try again.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

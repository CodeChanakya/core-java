# Exercises: Order Processing Queue

Work in a copy so the original stays intact:

```bash
cp -r 20-order-processing-queue ../my-order-processing-queue
cd ../my-order-processing-queue
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a sixth order, ORD-6, as express. Before running, predict where it comes out.

<details><summary>Hint</summary>Express orders come out in arrival order, so it goes after ORD-4.</details>

**B2.** Add `hasWaiting()` to `OrderDesk`.

<details><summary>Hint</summary>`!urgent.isEmpty()`.</details>

**B3.** After each order is handled, print how many are still waiting.

<details><summary>Hint</summary>`desk.waiting()` already exists.</details>

## Intermediate

**I1.** Add a `BULK` priority that is always served last.

<details><summary>Hint</summary>The comparator uses the enum's own order. Where does the new constant belong in the list?</details>

**I2.** Add `drain()` that returns every remaining order in the order it would be handled.

<details><summary>Hint</summary>Keep calling `nextMostUrgent()` until it returns `null`.</details>

## Challenge

**C1.** Make the history remember only the last three finished orders.

<details><summary>Hint</summary>`push` adds at the front, so the oldest sits at the far end: `pollLast()` drops it.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

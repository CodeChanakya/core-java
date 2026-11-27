# Exercises: Parallel Data Processing

Work in a copy so the original stays intact:

```bash
cp -r 33-parallel-data-processing ../my-parallel-data-processing
cd ../my-parallel-data-processing
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** How many readings are above 950? Predict it by hand first (49 per block of 1000), then check with a stream.

<details><summary>Hint</summary>`Arrays.stream(data).filter(v -> v > 950).count()`.</details>

**B2.** Find the smallest reading with a stream. What do you expect?

<details><summary>Hint</summary>Every block holds 0 to 999, so the smallest is 0.</details>

**B3.** Print the average reading to one decimal place, using `Locale.ROOT`.

<details><summary>Hint</summary>`(double) sum / count`, then `String.format(Locale.ROOT, "%.1f", ...)`. Expect 499.5.</details>

## Intermediate

**I1.** Add `histogram(data, chunks)` that counts readings per band of 100. Each chunk fills its own array and the arrays are added at the end.

<details><summary>Hint 1</summary>A `Callable<long[]>` per chunk.</details>
<details><summary>Hint 2</summary>`bands[data[i] / 100]++` inside the chunk, then add the arrays element by element.</details>

**I2.** Add `processFailFast` that cancels every other ticket as soon as one chunk fails.

<details><summary>Hint</summary>Catch the `ExecutionException`, loop over all tickets and call `cancel(true)`, then rethrow.</details>

## Challenge

**C1.** Find the maximum with divide and conquer: a `RecursiveTask` that splits the range in half until it is small, forks one half and computes the other.

<details><summary>Hint 1</summary>Extend `RecursiveTask<Integer>` and write `compute()`.</details>
<details><summary>Hint 2</summary>`left.fork()`, then `right.compute()`, then `left.join()`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

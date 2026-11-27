# Exercises: Background Report Generator

Work in a copy so the original stays intact:

```bash
cp -r 31-background-report-generator ../my-background-report-generator
cd ../my-background-report-generator
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a seventh report called Backup with 600 rows and print it.

<details><summary>Hint</summary>Add a `ReportRequest` and collect its ticket like the others.</details>

**B2.** Add `submit(ReportRequest)` to `ReportService` that returns the Future of a single report.

<details><summary>Hint</summary>`submitAll(List.of(request)).get(0)` is the quick way.</details>

**B3.** Print whether `main` and a new worker thread are daemons.

<details><summary>Hint</summary>`Thread.isDaemon()`. Threads you create are not daemons unless you say so.</details>

## Intermediate

**I1.** Add `buildAll(List<ReportRequest>)` that returns the reports in order, using `invokeAll`.

<details><summary>Hint</summary>`invokeAll` takes a list of `Callable`s and returns a list of Futures that are already finished.</details>

**I2.** Add `collectAll(requests)` returning a sorted map from name to either the summary or `FAILED: reason`.

<details><summary>Hint</summary>Catch `ExecutionException` inside the loop and use `getCause()`.</details>

## Challenge

**C1.** Write `runWithDeadline(Callable<String>, long ms)` returning an `Optional`. A job that takes longer is cancelled and gives an empty Optional.

<details><summary>Hint 1</summary>`get(ms, MILLISECONDS)` throws `TimeoutException`.</details>
<details><summary>Hint 2</summary>Call `cancel(true)` in the catch block before returning `Optional.empty()`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

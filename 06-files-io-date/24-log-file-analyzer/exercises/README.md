# Exercises: Log File Analyzer

Work in a copy so the original stays intact:

```bash
cp -r 24-log-file-analyzer ../my-log-file-analyzer
cd ../my-log-file-analyzer
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** For each log file, print the folder name and the file name without its `.log` ending.

<details><summary>Hint 1</summary>`getParent().getFileName()` for the folder.</details>
<details><summary>Hint 2</summary>`name.substring(0, name.lastIndexOf('.'))` for the bare name.</details>

**B2.** Add `percentOf(Level)` to `LogReport` and print the share of INFO lines.

<details><summary>Hint</summary>Divide by the number of **readable** lines, not all lines.</details>

**B3.** Before reading each file, print whether `Files.isRegularFile` is true for it.

<details><summary>Hint</summary>It returns `false` for folders and for paths that do not exist.</details>

## Intermediate

**I1.** Add `TRACE` to the `Level` enum. What changes in the summary, and why?

<details><summary>Hint</summary>Where should the new constant go, given that the order is the severity order?</details>

**I2.** Generalise `LogFolder` with `list(Path folder, String glob)` and use it to count the `*.txt` files.

<details><summary>Hint</summary>Move the body of `listLogs` into the new method and make `listLogs` call it.</details>

## Challenge

**C1.** Add a static `busiestComponent(List<String> lines)` that returns the component with the most lines in one file.

<details><summary>Hint 1</summary>Count with `merge` into a `TreeMap`, then pick the largest.</details>
<details><summary>Hint 2</summary>On a tie, the alphabetically first wins if you only replace the best on a strictly greater count.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder. It needs the `data` folder, so it runs from here.

# Exercises: Employee File Management

Work in a copy so the original stays intact:

```bash
cp -r 23-employee-file-management ../my-employee-file-management
cd ../my-employee-file-management
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Print the highest-paid employee after loading.

<details><summary>Hint</summary>Keep the best so far in a variable and compare with `compareTo` (a `BigDecimal` cannot use `>`).</details>

**B2.** Add `countLines(File)` that counts the lines of any file without storing them.

<details><summary>Hint</summary>Read with `readLine()` in a loop and add one each time.</details>

**B3.** Make the report end with a total salary line.

<details><summary>Hint</summary>Add the salaries in `writeReport`, then `printf` the total.</details>

## Intermediate

**I1.** Add `exportDepartment(File from, File to, String department)` that writes a new CSV containing only that department, and returns how many rows it wrote.

<details><summary>Hint</summary>`load`, filter into a new list, then `save`.</details>

**I2.** Reject a negative salary as a problem, with its line number.

<details><summary>Hint</summary>`salary.signum() < 0`. Add the problem and `return` from `parseRow`.</details>

## Challenge

**C1.** Add `merge(File first, File second, File out)`. When both files hold the same id, the second file's row wins. Return the row count.

<details><summary>Hint 1</summary>A `LinkedHashMap` from id to `Employee`.</details>
<details><summary>Hint 2</summary>`put` for the first file, then `put` for the second: a repeated key replaces the value and keeps its position.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder. It needs the `data` folder, so it runs from here.

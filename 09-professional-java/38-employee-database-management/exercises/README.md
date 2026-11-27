# Exercises: Employee Database Management

Work in a copy so the original stays intact:

```bash
cp -r 38-employee-database-management ../my-employee-database-management
cd ../my-employee-database-management
./run.sh
```

Edit the files under `src` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**A1.** Add the department `FINANCE` to the `Department` enum and hire someone into it. Does the schema need to change? Why or why not?

<details><summary>Hint</summary>The table stores the enum name as text. Nothing else changes, though a database with a fixed list of values would.</details>

**A2.** Add `findByNamePrefix(String)` to the repository, ignoring case. Implement it in both repositories and write one contract test that both must pass.

<details><summary>Hint 1</summary>In SQL: `WHERE LOWER(name) LIKE ?` with `prefix.toLowerCase(Locale.ROOT) + "%"` as the parameter.</details>
<details><summary>Hint 2</summary>Write the test in `EmployeeRepositoryContract`. It then runs against every implementation.</details>

**A3.** Read `EmployeeServiceTest.hireDateBoundary`. Change the clock in `setUp` to 1 July 2025 and predict which tests change. Then run them.

<details><summary>Hint</summary>Only a test that compares a date with today can change. Which test uses a date right next to the clock day?</details>

## Intermediate

**B1.** Add `averageSalary(Department)` to `EmployeeService`, rounded to the cent. If nobody works there, refuse with a `ValidationException` that names the department. Test both cases.

<details><summary>Hint</summary>`statsByDepartment()` already holds the average.</details>

**B2.** Add the rule that a raise may not push a salary above 500000.00. Write the failing test first, then the rule.

<details><summary>Hint</summary>Check after you work out the new salary and before you call `update`.</details>

## Challenge

**C1.** Write `CountingEmployeeRepository`, a decorator that wraps any repository, forwards every call and counts calls per method. Do not change any existing class. Then make a test subclass of the contract for it.

<details><summary>Hint 1</summary>It implements `EmployeeRepository` and holds another one in a field.</details>
<details><summary>Hint 2</summary>`EmployeeRepositoryContract` already tests behaviour; one extra test checks the counts.</details>

**C2.** Follow `GIT-WORKFLOW.md` for exercise A2: make a branch, commit the code and the test in two commits, and write a pull request description that says what changed and how you tested it.

<details><summary>Hint</summary>Two commits means the interface change and its implementations first, the test second, or the other way round. Each must leave the project compiling.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src). Run the solution version with `./solutions/run.sh`, or its tests with `./solutions/run.sh --test` (after `./run.sh --test` has downloaded the jars once).

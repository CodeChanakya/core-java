# Exercises: Employee Management System

Work in a copy so the original stays intact:

```bash
cp -r 10-employee-management-system ../my-employee-management-system
cd ../my-employee-management-system
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add one more `Developer` to the staff array in `Main` and check the totals by hand.

<details><summary>Hint</summary>The array literal is a comma-separated list; add a line.</details>

**B2.** Add `annualPay()` to `Employee` (twelve months of `monthlyPay()`). Print Asha's annual pay. Do you need to touch `Developer`?

<details><summary>Hint</summary>No: because the method calls `monthlyPay()`, each subclass's own rule is used automatically.</details>

**B3.** Change the on-call allowance to 5000.00 and predict the new total before running.

<details><summary>Hint</summary>The constant is in `Developer`. Which other lines of the report change?</details>

## Intermediate

**I1.** Add a `SalesExecutive` class: base salary plus 2 percent of this month's sales, passed to the constructor.

<details><summary>Hint 1</summary>Copy the shape of `Manager`.</details>
<details><summary>Hint 2</summary>Round the commission to 2 places with `HALF_UP`.</details>

**I2.** Add `countByRole(String role)` to `Payroll` and print how many developers there are.

<details><summary>Hint</summary>A loop and a counter, comparing `employee.role()` with `equals`.</details>

## Challenge

**C1.** Add a `Contractor`: paid hourly rate times hours this month, and no raise. Do not change `Payroll` at all.

<details><summary>Hint 1</summary>It needs its own `monthlyPay()` and an empty `applyRaise()`, like `Intern`.</details>
<details><summary>Hint 2</summary>Pass `BigDecimal.ZERO` to the parent constructor as the base salary.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

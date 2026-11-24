# Exercises: Employee Salary Calculator

Work in a copy so the original stays intact. Create `exercises/MySalary.java`, paste in the program (or the files you need), and rename the class to `MySalary`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MySalary.java
java -cp out MySalary
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Change the basic salary to a number of your choice and run it again.

<details><summary>Hint</summary>Only one line changes. Every other amount is calculated from it.</details>

**B2.** Add a performance bonus of 3000 to the gross salary.

<details><summary>Hint 1</summary>A new amount needs a variable, and the gross formula must include it.</details>
<details><summary>Hint 2</summary>Add the new variable to the `grossSalary` line, then check that the net salary moved by the same amount.</details>

**B3.** Print the net salary per working day.

<details><summary>Hint</summary>Divide the net salary by `workingDays`.</details>

## Intermediate

**I1.** Deduct income tax of 5% of the gross salary, and print the net salary after it.

<details><summary>Hint 1</summary>5% is `0.05`. Work out the tax first, as its own variable.</details>
<details><summary>Hint 2</summary>Net salary is now gross minus provident fund, professional tax and income tax.</details>

**I2.** Print attendance as a percentage with two decimals, such as `86.67%`.

<details><summary>Hint 1</summary>`26 * 100 / 30` loses the decimals. Make one number a `double`.</details>
<details><summary>Hint 2</summary>Write `daysWorked * 100.0 / workingDays`, and print it with `%.2f`. To print a literal percent sign in `printf`, write `%%`.</details>

## Challenge

**C1.** Give the employee an 8% raise on basic. Keep the new basic as an `int`, rounded to the nearest rupee (not cut off). Print the annual gross before and after.

<details><summary>Hint 1</summary>`Math.round` returns a `long`. You need an `int` variable.</details>
<details><summary>Hint 2</summary>A cast from `long` to `int` is allowed because you are choosing to do it: `(int) Math.round(...)`.</details>
<details><summary>Hint 3</summary>Annual gross is 12 times the monthly gross. Build the monthly gross twice, once with each basic.</details>

When you are done, compare with [`../solutions/MySalary.java`](../solutions/MySalary.java).

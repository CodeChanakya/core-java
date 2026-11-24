# 02 · Employee Salary Calculator

**Phase 02 · Core Fundamentals** &nbsp;|&nbsp; **Level:** Beginner &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: variables, `int` and `double`, arithmetic operators, casting, integer division.

---

## The problem

A payroll clerk has an employee's basic salary and a few rules: house rent allowance (HRA) is 40% of basic, provident fund is 12% of basic, a flat professional tax is deducted, and the employee is paid only for the days worked. The clerk wants one salary slip that shows every step.

The program is a straight line of calculations. Its real job is to show you how Java treats numbers, including two places where it quietly does something you may not expect.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`src/main/java/SalaryCalculator.java`](src/main/java/SalaryCalculator.java) | Each variable is built from the ones above it. Read top to bottom. |
| 2 | [`expected-output.txt`](expected-output.txt) | The slip a correct run prints. |
| 3 | [`broken/`](broken/README.md) | Three small mistakes about numbers. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Expected output:

```text
===== SALARY SLIP =====
Basic salary      : 50000
HRA (40%)         : 20000.00
Special allowance : 5000
Gross salary      : 75000.00
Provident fund    : 6000.00
Professional tax  : 200
Net salary        : 68800.00

===== ATTENDANCE =====
Days worked       : 26 of 30
Wrong percentage  : 0
Right percentage  : 86
Payable amount    : 59626.67
Cut off to whole  : 59626
Rounded to whole  : 59627

Net salary, 12 months: 825600.00
```

## The two number types you need now

| Type | Holds | Example | Used here for |
|---|---|---|---|
| `int` | whole numbers, about -2.1 billion to 2.1 billion | `50000` | salary in whole rupees, day counts |
| `double` | numbers with a decimal point | `0.40`, `20000.0` | percentages and anything that may have decimals |
| `long` | whole numbers, much larger than `int` | `59627` | what `Math.round` returns |

When an `int` and a `double` meet in one calculation, Java converts the `int` to `double` first, so the answer is a `double`. That is why `basicSalary * 0.40` works with no extra effort.

## Operators, in the order Java applies them

```text
 ( )          brackets first
 *  /  %      then multiplication, division, remainder (left to right)
 +  -         then addition and subtraction (left to right)
```

So `daysWorked * 100 / workingDays` means `(daysWorked * 100) / workingDays`.

## The integer division trap

If both sides of `/` are `int`, Java divides and throws away the remainder.

```text
26 / 30 * 100   ->   (26 / 30) * 100   ->   0 * 100   ->   0     wrong
26 * 100 / 30   ->   2600 / 30         ->   86                    right (as a whole number)
```

The first version divides too early: `26 / 30` is `0` in whole numbers, and everything after that is `0`. The second multiplies first, so the division happens on a large number and loses much less. To keep the decimals, make one side a `double`: `26 * 100.0 / 30` gives `86.666...`.

## Casting and rounding are different

```java
int payableCutOff = (int) payable;           // 59626.67 becomes 59626
long payableRounded = Math.round(payable);   // 59626.67 becomes 59627
```

`(int)` is a *cast*: it tells Java "I know this loses the decimals, do it anyway". It always cuts off, never rounds. `Math.round` rounds to the nearest whole number and returns a `long`.

Without the cast, `int x = 59626.67;` is a compile error. Java refuses to lose information unless you say so.

## Why `Locale.US` appears in the printing

`%.2f` prints a `double` with two decimals. In some countries the decimal separator is a comma, so the same program would print `20000,00` there. `Locale.US` pins the format so the output is identical everywhere, which also lets `./run.sh --check` work on any computer. That is the reason for `import java.util.Locale;` at the top.

## What is in memory

```text
main's workspace
+------------------+-----------+
| basicSalary      | 50000     |   int: the number itself
| hra              | 20000.0   |   double: the number itself
| daysWorked       | 26        |
| payableCutOff    | 59626     |
+------------------+-----------+
```

All of these are primitive values, stored directly in the workspace. Compare this with the Strings in Program 1, which were stored elsewhere and pointed to.

## Common mistakes

| Mistake | What you see |
|---|---|
| `int bonus = 2500.75;` | `error: incompatible types: possible lossy conversion from double to int` |
| Using a variable that was never given a value | `error: variable total might not have been initialized` |
| Dividing two `int` values and expecting decimals | No error. The answer is simply wrong. |
| Writing `%` in a `printf` format for a literal percent sign | Write `%%` instead |

Try all three number mistakes yourself in [`broken/`](broken/README.md).

## Practice

- [`exercises/`](exercises/README.md) has six tasks: three beginner, two intermediate, one challenge.
- [`solutions/MySalary.java`](solutions/MySalary.java) is one answer to all six. Try them first.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 3 is a short lab on why `double` is the wrong type for money, and what to use instead. The slip above keeps rupees in `double` only so the arithmetic stays easy to read.

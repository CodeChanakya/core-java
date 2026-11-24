# 06 · Student Marks Analyzer

**Phase 02 · Core Fundamentals** &nbsp;|&nbsp; **Level:** Beginner to intermediate &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: arrays, two-dimensional arrays, `for` and for-each loops, method overloading, the `length` field.

---

## The problem

A teacher has four students and four subjects. She wants a report card line for each student (total, average, grade), and some figures for the whole class: the class average, the highest mark, the topper, and the average for each subject.

Until now every value has had its own variable. That stops working the moment there are 4 students, 40 or 400. An *array* holds many values of the same type under one name.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`src/main/java/MarksAnalyzer.java`](src/main/java/MarksAnalyzer.java) | The two arrays at the top of `main`, then the loop that prints rows. |
| 2 | the small methods below `main` | `total`, `average`, `highest`: each does one job. |
| 3 | [`expected-output.txt`](expected-output.txt) | Verify one row by hand: Asha's total is 92 + 88 + 95 + 90. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Expected output:

```text
Name        Maths  Science  English  History  Total  Average  Grade
Asha           92       88       95       90    365    91.25      A
Rahul          67       54       71       60    252    63.00      C
Meera          81       79       85       77    322    80.50      B
Kiran          45       38       52       41    176    44.00      D

Class average : 69.69
Highest mark  : 95
Topper        : Asha (average 91.25)

Average per subject
  Maths    71.25
  Science  64.75
  English  75.75
  History  67.00
```

## How the marks are stored

```text
marks  (int[][])      subject:  [0]    [1]     [2]      [3]
                               Maths Science English History
 student [0]  Asha               92     88      95       90
 student [1]  Rahul              67     54      71       60
 student [2]  Meera              81     79      85       77
 student [3]  Kiran              45     38      52       41

 marks[1][2]   is 71        Rahul's English mark
 marks[1]      is a whole row: the int[] holding Rahul's four marks
 marks.length  is 4         the number of rows (students)
 marks[1].length is 4        the number of marks in one row
```

Positions start at **0**, not 1. The first student is `marks[0]` and the last is `marks[3]`, one less than `length`.

## Two arrays kept in step

`students[1]` is `"Rahul"` and `marks[1]` is Rahul's row. They match only because both lists are in the same order. This works, but it is fragile: if you sort one and not the other, names and marks no longer belong together. Phase 3 replaces this pattern with a `Student` object that holds its own name and marks.

## Two kinds of loop

```java
for (int i = 0; i < students.length; i++) { ... }   // you need the position i
for (int mark : marks) { ... }                      // you only need each value
```

Use for-each when you only read the values. Use the counted `for` when you need the index, for example to look up `students[i]` and `marks[i]` together.

## Overloading

```java
static double average(int[] marks)       // one student's average
static double average(int[][] table)     // the whole class's average
```

Two methods may share a name if their parameter types differ. Java chooses by looking at what you pass: `average(marks[0])` is an `int[]`, `average(marks)` is an `int[][]`. The same trick is used for `highest`. Readers see one idea, "average", instead of two invented names.

## One line worth reading slowly

```java
return (double) total(marks) / marks.length;
```

Without the `(double)`, both sides are `int` and the division throws the decimals away: 365 / 4 would be 91, not 91.25. The cast applies to `total(marks)` first, so the division happens in `double`.

## Grades

```text
average >= 90   A
average >= 75   B
average >= 60   C
average >= 40   D
otherwise       F
```

The chain is checked from the top, so Asha's 91.25 stops at the first test. Order matters: if `>= 40` came first, everybody would be a D.

## Common mistakes

| Mistake | What you see |
|---|---|
| `i <= marks.length` in a loop | `ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 4` |
| `marks.length()` with brackets | `error: cannot find symbol` (for arrays, `length` has no brackets) |
| `{92, 88.5, 95}` in an `int[]` | `error: possible lossy conversion from double to int` |
| Dividing two `int` values for an average | No error, but the decimals vanish |

Try three of these in [`broken/`](broken/README.md).

## Practice

- [`exercises/`](exercises/README.md) has six tasks: three beginner, two intermediate, one challenge.
- [`solutions/MyMarks.java`](solutions/MyMarks.java) is one answer to the exercises. Try them first.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 7 is the Phase 2 project. It brings variables, `BigDecimal`, decisions, loops, methods, arrays and Strings together in one receipt.

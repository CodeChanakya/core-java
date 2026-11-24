# Exercises: Student Marks Analyzer

Work in a copy so the original stays intact. Create `exercises/MyMarks.java`, paste in the program (or the files you need), and rename the class to `MyMarks`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MyMarks.java
java -cp out MyMarks
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a fifth student with four marks.

<details><summary>Hint</summary>A new name in `students`, and a new row in `marks`. Both lists must stay in the same order.</details>

**B2.** Print each student's lowest mark. Write a `lowest` method like `highest`.

<details><summary>Hint 1</summary>Start with the first mark as the best guess, then compare it with the rest.</details>
<details><summary>Hint 2</summary>It is `highest` with the comparison turned around.</details>

**B3.** Print the topper's name after the table.

<details><summary>Hint</summary>The topper is the student with the highest average. Remember a position, not just a value.</details>

## Intermediate

**I1.** Add a Result column: a student passes only if every subject mark is at least 40.

<details><summary>Hint</summary>A student passes if their lowest mark is at least 40.</details>

**I2.** Print how many students failed.

<details><summary>Hint</summary>A counter that starts at 0 and goes up by one each time a student fails.</details>

## Challenge

**C1.** Print the students ranked by average, best first. Do not use `Arrays.sort`.

<details><summary>Hint 1</summary>Sort a list of row numbers, not the marks, so each name stays matched to its row.</details>
<details><summary>Hint 2</summary>Repeatedly compare neighbours and swap when they are in the wrong order. This is called bubble sort.</details>
<details><summary>Hint 3</summary>When you swap, swap the row numbers: `int temp = order[i]; order[i] = order[i + 1]; order[i + 1] = temp;`</details>

When you are done, compare with [`../solutions/MyMarks.java`](../solutions/MyMarks.java).

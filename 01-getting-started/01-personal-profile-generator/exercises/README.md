# Exercises: Personal Profile Generator

Work in a copy so the original stays intact. Create a new file `exercises/MyProfile.java`, paste in the program, and rename the class to `MyProfile` (the class name and file name must match).

Compile and run from inside this folder:

```bash
javac -d out MyProfile.java
java -cp out MyProfile
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1. Make it yours.** Change every value to your own details.

<details><summary>Hint</summary>Only the text between the quotes changes. Leave the quotes alone.</details>

**B2. Add a hobby.** Add a `hobby` variable and a line that prints it.

<details><summary>Hint 1</summary>A new fact needs two things: a variable, and a <code>println</code> that uses it.</details>
<details><summary>Hint 2</summary>Copy the pattern of the <code>city</code> variable and the City line.</details>

**B3. Add breathing room.** Print an empty line between the header and the first fact.

<details><summary>Hint</summary><code>System.out.println();</code> with nothing inside the brackets prints just a line break.</details>

## Intermediate

**I1. Join two values.** Add a `country` variable and change the City line so it prints `Pune, India`.

<details><summary>Hint</summary>You can use <code>+</code> more than once in one line. Remember the comma and space are text too.</details>

**I2. Do a little arithmetic.** Print an extra line showing the age in months.

<details><summary>Hint 1</summary>Multiplication uses <code>*</code>.</details>
<details><summary>Hint 2</summary>Inside <code>println</code> you can write <code>"Age in months : " + age * 12</code>. Notice that <code>*</code> happens before <code>+</code>.</details>

## Challenge

**C1. Line up the colons.** Add a label called `Favourite language`. The longest label is now much longer than `Name`, so the colons drift out of line. Make every colon land in the same column.

<details><summary>Hint 1</summary>Search for how <code>System.out.printf</code> works. It takes a format string and then the values.</details>
<details><summary>Hint 2</summary>In a format string, <code>%-20s</code> means "print this text, padded with spaces on the right up to 20 characters".</details>
<details><summary>Hint 3</summary>Use <code>%n</code> at the end of the format for the line break. A full line looks like <code>System.out.printf("%-20s: %s%n", "Name", name);</code></details>

When you are done, compare with [`../solutions/MyProfile.java`](../solutions/MyProfile.java).

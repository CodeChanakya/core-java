# Exercises: ATM Menu Simulation

Work in a copy so the original stays intact. Create `exercises/MyAtm.java`, paste in the program (or the files you need), and rename the class to `MyAtm`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MyAtm.java
java -cp out MyAtm
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Change the PIN and the starting balance.

<details><summary>Hint</summary>They are a constant and a variable at the top of the program. Remember `sample-input.txt` must match the new PIN.</details>

**B2.** Add a menu option "Show daily limit" that only prints a message, and move Exit to option 5.

<details><summary>Hint</summary>A new menu line, a new `case`, and a changed Exit case.</details>

**B3.** After a successful withdrawal, print how many 100-rupee notes were dispensed.

<details><summary>Hint</summary>Whole notes: `amount / NOTE_VALUE`. Both are `int`, which is exactly the division you want here.</details>

## Intermediate

**I1.** Add a daily withdrawal limit of 5000. Refuse a withdrawal that would take the total for the session past it.

<details><summary>Hint 1</summary>`main` needs a variable for the total withdrawn so far.</details>
<details><summary>Hint 2</summary>Pass "how much is still allowed" into `withdraw` as a new parameter.</details>
<details><summary>Hint 3</summary>Update the total only when the balance actually went down.</details>

**I2.** Add a "Change PIN" option. The new PIN must be four digits.

<details><summary>Hint 1</summary>The PIN can no longer be a constant. Make it a variable in `main` and pass it into `pinAccepted`.</details>
<details><summary>Hint 2</summary>A four-digit number is between 1000 and 9999.</details>

## Challenge

**C1.** On exit, print a summary: how many deposits and withdrawals happened and the total amount moved. Do it without any `static` variables.

<details><summary>Hint 1</summary>`deposit` and `withdraw` already return the new balance. Compare it with the old one.</details>
<details><summary>Hint 2</summary>If the balance went up, a deposit happened; if it went down, a withdrawal happened.</details>

When you are done, compare with [`../solutions/MyAtm.java`](../solutions/MyAtm.java).

## Testing your changes

Write the answers you would type into a text file, one per line, and feed it to your program:

```bash
javac -d out MyAtm.java
java -cp out MyAtm < my-input.txt
```

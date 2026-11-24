# Exercises: Electricity Bill Generator

Work in a copy so the original stays intact. Create `exercises/MyBill.java`, paste in the program (or the files you need), and rename the class to `MyBill`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MyBill.java
java -cp out MyBill
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a fourth slab: units above 300 cost 9.10 each.

<details><summary>Hint 1</summary>A new slab is one more `if` block at the top of `billFor`, and one more rate constant.</details>
<details><summary>Hint 2</summary>The top slab must be checked first, and it cuts `remaining` back to 300.</details>

**B2.** In `Main`, add readings for 200 and 201 units. Check them by hand.

<details><summary>Hint</summary>200 units: 100 x 3.50 + 100 x 5.25 = 875.00.</details>

**B3.** Make `slabFor` return "slab 4" for the new top slab.

<details><summary>Hint</summary>Add one more `else if` before the final `else`.</details>

## Intermediate

**I1.** Add a fixed monthly charge of 50.00 and 5% tax on the subtotal. Print each line of the bill.

<details><summary>Hint 1</summary>Subtotal = energy charge + fixed charge. Tax is 5% of the subtotal.</details>
<details><summary>Hint 2</summary>Round the tax to 2 places with `setScale(2, RoundingMode.HALF_UP)`, as in Program 3.</details>

**I2.** Give the slab limits names (such as `SLAB_1_LIMIT`) so that no number appears twice in the code.

<details><summary>Hint</summary>The numbers 100, 200 and 300 each appear in more than one place.</details>

## Challenge

**C1.** Support household and commercial tariffs without copying `billFor`.

<details><summary>Hint 1</summary>Different customers only differ in their rates. What if the rates were parameters?</details>
<details><summary>Hint 2</summary>Give `billFor` one parameter per rate, and call it twice with different rates.</details>

When you are done, compare with [`../solutions/MyBill.java`](../solutions/MyBill.java).

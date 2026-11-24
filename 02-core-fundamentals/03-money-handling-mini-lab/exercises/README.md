# Exercises: Money Handling Mini-Lab

Work in a copy so the original stays intact. Create `exercises/MyInvoice.java`, paste in the program (or the files you need), and rename the class to `MyInvoice`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MyInvoice.java
java -cp out MyInvoice
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Change the invoice to 2 items at 1299.50.

<details><summary>Hint</summary>Change the String inside `new BigDecimal("...")` and the `quantity` variable.</details>

**B2.** Print the GST rate as a percentage, for example `GST (18%)`.

<details><summary>Hint 1</summary>The rate is stored as `0.18`. Multiply by 100 to get 18.</details>
<details><summary>Hint 2</summary>`gstRate.multiply(BigDecimal.valueOf(100)).intValue()` gives a plain `int` you can join into the text.</details>

**B3.** Change the GST rate to 5%.

<details><summary>Hint</summary>The rate is a String: `"0.05"`.</details>

## Intermediate

**I1.** Give a 10% discount on the subtotal, taken off before GST. Print the discount line and the total.

<details><summary>Hint</summary>Work out the discount, round it to 2 places, then subtract it. Charge GST on what is left.</details>

**I2.** Split 1000.00 equally among 3 people with `divide`. Add the three shares and print how much is missing.

<details><summary>Hint 1</summary>`divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP)`.</details>
<details><summary>Hint 2</summary>Subtract the sum of the shares from 1000.00.</details>

## Challenge

**C1.** Fix the split so the three shares add up to exactly 1000.00. The first person takes the leftover paisa.

<details><summary>Hint 1</summary>Give two people the normal share. The first person gets whatever remains.</details>
<details><summary>Hint 2</summary>First share = total minus two normal shares.</details>

When you are done, compare with [`../solutions/MyInvoice.java`](../solutions/MyInvoice.java).

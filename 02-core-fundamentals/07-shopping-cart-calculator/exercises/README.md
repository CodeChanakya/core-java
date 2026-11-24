# Exercises: Shopping Cart Calculator

Work in a copy so the original stays intact. Create `exercises/MyCart.java`, paste in the program (or the files you need), and rename the class to `MyCart`. The class name and the file name must match.

Compile and run from inside this `exercises` folder:

```bash
javac -d out MyCart.java
java -cp out MyCart
```

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Change one item: a new name, quantity and price.

<details><summary>Hint</summary>The three arrays are matched by position. Change the same position in each.</details>

**B2.** Print the total number of items in the cart (the sum of the quantities).

<details><summary>Hint</summary>A counter that starts at 0 and adds each quantity inside a loop.</details>

**B3.** Change the coupon in `main` to `"Save10"` and then to `"SAVE 10"`. Explain why one works and the other does not.

<details><summary>Hint</summary>Look at what `trim()` and `toUpperCase()` do and do not remove.</details>

## Intermediate

**I1.** Charge 50.00 shipping, unless the subtotal is at least 1500.00. Add a Shipping line.

<details><summary>Hint 1</summary>`BigDecimal` is compared with `compareTo`, never with `<` or `>=`.</details>
<details><summary>Hint 2</summary>`subtotal.compareTo(limit) >= 0` means "subtotal is at least the limit".</details>

**I2.** Add a second coupon, `FLAT200`: 200.00 off, but only when the subtotal is at least 2000.00.

<details><summary>Hint</summary>Two conditions must both be true: use `&&`.</details>

## Challenge

**C1.** Item names longer than 16 characters break the columns. Cut them to fit and end them with `...`.

<details><summary>Hint 1</summary>`name.length()` tells you how long a name is.</details>
<details><summary>Hint 2</summary>`name.substring(0, 13)` gives the first 13 characters. Add `"..."` to make 16.</details>

When you are done, compare with [`../solutions/MyCart.java`](../solutions/MyCart.java).

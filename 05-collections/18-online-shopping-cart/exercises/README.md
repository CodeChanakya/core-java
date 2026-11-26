# Exercises: Online Shopping Cart

Work in a copy so the original stays intact:

```bash
cp -r 18-online-shopping-cart ../my-online-shopping-cart
cd ../my-online-shopping-cart
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a seventh product, then add the same SKU again. What happens to the line count?

<details><summary>Hint</summary>Look at the loop in `add`: a known SKU only changes the quantity.</details>

**B2.** Add `contains(String sku)` to `ShoppingCart`.

<details><summary>Hint</summary>Loop over `items` and compare SKUs with `equals`.</details>

**B3.** Print the name of the last item in the cart.

<details><summary>Hint</summary>The last position is `size() - 1`.</details>

## Intermediate

**I1.** Add a comparator for cheapest unit price first and print the cart in that order.

<details><summary>Hint</summary>`Comparator.comparing(CartItem::getUnitPrice)`. `BigDecimal` is already comparable.</details>

**I2.** Add `updateQuantity(String sku, int quantity)` that removes the line when the quantity is zero.

<details><summary>Hint 1</summary>You will need a way to change a quantity outright. Add a setter to `CartItem` that refuses zero or less.</details>
<details><summary>Hint 2</summary>Use the iterator again so removal is safe.</details>

## Challenge

**C1.** Add `mergeFrom(ShoppingCart other)` that adds the other cart's lines to this one, merging matching SKUs, and leaves the other cart untouched.

<details><summary>Hint 1</summary>Loop over `other.view()` and call your own `add` for each line.</details>
<details><summary>Hint 2</summary>`add` takes the price as a String. `getUnitPrice().toPlainString()` gives you one.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

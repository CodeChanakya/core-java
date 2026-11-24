# Exercises: E-commerce Order Management

Work in a copy so the original stays intact:

```bash
cp -r 13-ecommerce-order-management ../my-ecommerce-order-management
cd ../my-ecommerce-order-management
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a new physical product to the catalog and order it.

<details><summary>Hint</summary>`new PhysicalProduct(sku, name, price, stock, weightKg)`.</details>

**B2.** Print the customer's email on the invoice heading.

<details><summary>Hint</summary>`Order.invoice()` already has `customer`.</details>

**B3.** Raise the loyalty discount to 10 percent. Predict Asha's new total.

<details><summary>Hint</summary>The constant is in `Customer`.</details>

## Intermediate

**I1.** Add `printLowStock(int threshold)` to `Catalog`, listing products with that many units or fewer.

<details><summary>Hint</summary>Loop and compare `getStock()`.</details>

**I2.** Add a way to cancel an order that is still `NEW`. It must put the stock back, and a shipped order must refuse.

<details><summary>Hint 1</summary>The order needs a `CANCELLED` status and a guarded method like `markPaid`.</details>
<details><summary>Hint 2</summary>Putting stock back is the reverse of `removeStock`.</details>

## Challenge

**C1.** Let a customer remember their own orders and print them. Who should record each order, and when?

<details><summary>Hint 1</summary>An `ArrayList<Order>` inside `Customer`.</details>
<details><summary>Hint 2</summary>`OrderService` creates the order, so it is the natural place to tell the customer about it.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

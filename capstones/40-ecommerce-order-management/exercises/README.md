# Exercises: E-commerce Order Management

1. **A new offer.** Add `LOYALTY`: two percent off for any order of 2,000 or more. Add it to `Discounts.standard()`.
   Hint: copy `BIG_ORDER` and change two numbers. Add the boundary tests (1,999.99 and 2,000).
2. **Sales per day.** Add `Reports.salesPerDay(orders)` returning a `Map<LocalDate, Long>`, oldest day first,
   cancelled orders left out. Hint: `groupingBy`, `TreeMap::new`, `counting()`.
3. **Repeat customers.** Add `Reports.repeatCustomers(orders)`: the ids of customers with two or more sales.
4. **Shipping.** Add 99.00 shipping to every order under 1,000. Decide where it belongs: in `Order`, in a new
   policy, or in `OrderService`. Write down why.
5. **Return an order.** Add `DELIVERED -> RETURNED`, with the stock going back. You will touch `OrderStatus`,
   `OrderService` and the store. Which tests tell you what you missed?
6. **A second gateway.** Write `RetryingGateway` that wraps another gateway and tries twice before giving up.
   Test it with a lambda that fails once and then succeeds.

Solutions for tasks 2 and 3 are in [`../solutions`](../solutions/README.md). Try first.

# Exercises: Concurrent Order Processing System

1. **A second timeout.** Give the confirmation its own timeout of 200 ms. If it is slow, the order is still
   COMPLETED, with the detail "paid, but the confirmation was slow". Hint: `orTimeout` on the notifier's future,
   before `handle`.
2. **Retry once.** Write `RetryingGateway`, which wraps another gateway and tries again if the first try fails with an
   exception. A decline is an answer, so it is not retried. Test it with a gateway that fails once.
3. **Summary.** Write `Summary.countByStatus(results)` with every status present, zeros included.
4. **Priority.** Orders for a gold customer should be processed before others when the pool is busy. What in the
   design has to change? Hint: the executor's queue.
5. **Two products in one order.** Reserve both or neither. If the second reservation fails, release the first. Write
   the failing test first.
6. **Prove it breaks.** Replace `compareAndSet` with a plain `set` in `reserve`. Run the tests ten times. How often
   do they notice? What would make the test more likely to catch it?

Solutions for tasks 2 and 3 are in [`../solutions`](../solutions/README.md). Try first.

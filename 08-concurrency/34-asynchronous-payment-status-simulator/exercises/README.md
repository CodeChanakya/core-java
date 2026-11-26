# Exercises: Asynchronous Payment Status Simulator

Work in a copy so the original stays intact:

```bash
cp -r 34-asynchronous-payment-status-simulator ../my-asynchronous-payment-status-simulator
cd ../my-asynchronous-payment-status-simulator
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Process one more payment, P-4001 for 100 rupees by UPI from ACC-A, and print its status.

<details><summary>Hint</summary>`PaymentRequest.of("P-4001", "ACC-A", "100", "upi")`.</details>

**B2.** Print the reason of every payment that did not complete.

<details><summary>Hint</summary>Process each request, then print `result.detail()` for any status other than `COMPLETED`.</details>

**B3.** Predict the status of a 45,000 rupee card payment from ACC-A, then run it.

<details><summary>Hint</summary>The fraud limit is 90,000 and the account holds 100,000.</details>

## Intermediate

**I1.** Add a third parallel check: a daily limit of 50,000 rupees. Decline anything above it with a clear reason.

<details><summary>Hint 1</summary>A new `LimitService` that returns a `CompletableFuture<Boolean>`.</details>
<details><summary>Hint 2</summary>Start all three checks first, then `allOf(...)` and read the values with `join()` inside the `thenApply`.</details>

**I2.** Write `Retry.withDelay` that waits between attempts without blocking a thread.

<details><summary>Hint 1</summary>`CompletableFuture.delayedExecutor(ms, MILLISECONDS)` runs a task later.</details>
<details><summary>Hint 2</summary>Chain the next attempt with `thenCompose`.</details>

## Challenge

**C1.** Write `processThrottled(requests, maxInFlight)` so that at most three payments are in flight at once. Prove it with a counter.

<details><summary>Hint 1</summary>A `Semaphore` with 3 permits. Acquire before starting a payment.</details>
<details><summary>Hint 2</summary>Release the permit in `whenComplete`. Track the most in flight with `AtomicInteger.accumulateAndGet(..., Math::max)`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder.

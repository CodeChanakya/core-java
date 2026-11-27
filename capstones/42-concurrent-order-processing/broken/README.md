# Break it on purpose

Each file needs only the JDK:

```
javac --release 21 -d /tmp/out LostUpdate.java
java -cp /tmp/out LostUpdate
```

## LostUpdate

```
Two threads each added 1 to 0. Expected 2, got 1
```

Both threads read 0, both wrote 1. The short sleep between reading and writing makes this happen every time, so
you can see it. Without the sleep it happens only sometimes, which is worse: the bug hides until the busiest day.
The fix is to make read and write one atomic step, as `AtomicInteger.incrementAndGet()` or the `compareAndSet`
loop in `Inventory.reserve` does.

## PoolStarvation

```
Nothing happened for one second. The only thread is waiting for itself.
```

A task running on the pool's only thread asked the same pool to run a second task, and then waited for it. The
second task can never start, because the one thread is busy waiting. The program uses a daemon thread, because a
thread blocked in `join()` cannot be interrupted, and would otherwise keep the program from ending. The fix is to
chain with `thenCompose` instead of waiting inside a task.

## SilentFailure

```
The task failed 300 ms ago, and nothing was printed.
isCompletedExceptionally: true
join() threw: CompletionException
The real cause is inside it: java.lang.IllegalStateException: payment gateway exploded
```

An exception inside an async task is not thrown anywhere. It is stored in the future, and nobody sees it until
someone asks. Every chain needs an ending: `exceptionally`, `handle` or `whenComplete`. And the error arrives
wrapped in a `CompletionException`, which is why `OrderProcessor` has `unwrap`.

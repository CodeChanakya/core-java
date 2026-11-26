# Break it on purpose

Each file here is a classic concurrency bug. The first one races for real, so its numbers will differ on your machine.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `UnsafeCounter.java`: updates lost to a race

```bash
javac UnsafeCounter.java
java UnsafeCounter
```

```text
Attempt 1: expected 400000 but got 343548
Updates lost: 56452 (your number will differ)
```

Four threads each add 1 to a shared `int` 100,000 times. The answer should be 400,000. `count++` is read, add, write, so threads overwrite each other's work. The number of lost updates changes every run, which is the nature of a race. Fix it with `AtomicInteger`, `LongAdder` or a lock.

## 2. `WaitOutsideSynchronized.java`: wait() without owning the monitor

```bash
javac WaitOutsideSynchronized.java
java WaitOutsideSynchronized
```

```text
Exception in thread "main" java.lang.IllegalMonitorStateException: current thread is not owner
	at java.base/java.lang.Object.wait0(Native Method)
	at java.base/java.lang.Object.wait(Object.java:366)
	at WaitOutsideSynchronized.main(WaitOutsideSynchronized.java:8)
```

`wait()` and `notify()` are only legal for a thread that holds the object's monitor, that is, inside `synchronized (lock)`. Prefer a `BlockingQueue` or another high-level tool and you will rarely need them.

## 3. `Deadlock.java`: two locks in opposite orders

```bash
javac Deadlock.java
java Deadlock
```

```text
a-to-b is BLOCKED
b-to-a is BLOCKED
Threads the JVM reports as deadlocked: 2
```

Each thread holds one lock and waits for the other. A latch makes sure both hold their first lock before asking for the second, so it happens every time. Both threads are `BLOCKED` and the JVM itself reports two deadlocked threads. The cure is a fixed lock order, as in `Bank.transfer`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

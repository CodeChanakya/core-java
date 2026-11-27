# Break it on purpose

Each file here breaks one rule of sharing work between threads. The first one races, so its numbers will differ on your machine.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `SharedArrayList.java`: many threads adding to one ArrayList

```bash
javac SharedArrayList.java
java SharedArrayList
```

```text
Attempt 1: expected 100000 elements but found 30789
(your numbers will differ)
```

`ArrayList` is not thread-safe. Concurrent `add` calls overwrite each other's slot or resize at the wrong moment, so elements go missing, and sometimes the call throws. Collect into a thread-safe structure, or let the stream collect for you (`.toList()`, or `Collectors`).

## 2. `PoolStarvation.java`: a task waiting for another task in the same pool

```bash
javac PoolStarvation.java
java PoolStarvation
```

```text
Nothing happened for one second: TimeoutException
The outer task is waiting for the inner task, which is waiting for a thread.
```

The pool has one thread. The outer task occupies it while waiting for the inner task, which needs a thread to run. Nothing can move. `get` with a timeout turned a silent hang into a visible `TimeoutException`. Use a bigger pool, a separate pool, or do not wait inside a task.

## 3. `UnhandledGetExceptions.java`: ignoring the exceptions of Future.get()

```bash
javac UnhandledGetExceptions.java
```

```text
UnhandledGetExceptions.java:12: error: unreported exception InterruptedException; must be caught or declared to be thrown
        System.out.println("The answer is " + answer.get());   // BUG: InterruptedException and ExecutionException
                                                        ^
1 error
```

`get()` can throw `InterruptedException` (you were told to stop waiting) and `ExecutionException` (the task failed). Both are checked. Catch them, or declare `throws`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

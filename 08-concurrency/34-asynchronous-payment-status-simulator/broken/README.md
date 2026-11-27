# Break it on purpose

Each file here breaks one rule of `CompletableFuture`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `JoinWrapsException.java`: the exception you catch is not the one that was thrown

```bash
javac JoinWrapsException.java
java JoinWrapsException
```

```text
What you catch : CompletionException
Its message    : java.lang.IllegalStateException: card declined
The real cause : IllegalStateException: card declined
```

A failure inside a stage is wrapped in a `CompletionException` when it reaches `join()`. The message of the wrapper repeats the original, but the type is different. Always look at `getCause()`, which is what `PaymentPipeline.failure` does.

## 2. `MainEndsFirst.java`: main ends before the async work

```bash
javac MainEndsFirst.java
java MainEndsFirst
```

```text
main() finished.
```

Nothing waits for the future, `main` returns, and the daemon thread doing the work is stopped with the JVM. The line inside `thenAccept` never prints. Wait at the edge with `join()` or a deadline. (The common pool uses daemon threads on any machine with two or more cores; the demonstration creates them by hand so it behaves identically everywhere.)

## 3. `ThenApplyNesting.java`: thenApply with a function that returns a future

```bash
javac ThenApplyNesting.java
```

```text
ThenApplyNesting.java:16: error: incompatible types: inference variable U has incompatible bounds
        CompletableFuture<String> name = id.thenApply(n -> lookUpName(n));   // BUG: should be thenCompose
                                                     ^
    equality constraints: String
    lower bounds: CompletableFuture<String>
  where U,T are type-variables:
    U extends Object declared in method <U>thenApply(Function<? super T,? extends U>)
    T extends Object declared in class CompletableFuture
1 error
```

`lookUpName` already returns a `CompletableFuture<String>`. `thenApply` wraps that again, giving a future inside a future, which cannot be assigned to `CompletableFuture<String>`. Use `thenCompose`, which flattens it.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

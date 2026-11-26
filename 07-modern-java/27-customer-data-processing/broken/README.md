# Break it on purpose

Each file here breaks one rule of lambdas and streams.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ModifyCapturedVariable.java`: changing a captured variable

```bash
javac ModifyCapturedVariable.java
```

```text
ModifyCapturedVariable.java:9: error: local variables referenced from a lambda expression must be final or effectively final
        names.forEach(name -> count++);
                              ^
1 error
```

A lambda may read a local variable only if it is effectively final. `count` is changed inside the lambda, so the compiler refuses. Use `stream().count()`, or `mapToInt(...).sum()`, and let the stream do the counting.

## 2. `ReuseStream.java`: using a stream twice

```bash
javac ReuseStream.java
java ReuseStream
```

```text
How many: 3
Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:229)
	at java.base/java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647)
	at ReuseStream.main(ReuseStream.java:10)
```

A stream is a one-shot pipeline. After the terminal operation it is spent. Create a new stream from the source each time, or keep the *list* and stream it as often as you like.

## 3. `NoTerminalOperation.java`: a pipeline with no terminal step

```bash
javac NoTerminalOperation.java
java NoTerminalOperation
```

```text
The pipeline above printed nothing.
Passed the filter: Rahul
Passed the filter: Meera
This one ran, because count() is terminal: 2
```

The first pipeline has `filter` and `peek` but no terminal step, so it never runs and prints nothing. Intermediate steps only describe the work. Adding `count()` starts it.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

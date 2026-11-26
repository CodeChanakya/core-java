# Break it on purpose

Each file here is a classic mistake with files. They use temporary files, so nothing is left behind.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `UnclosedWriter.java`: a writer that is never closed

```bash
javac UnclosedWriter.java
java UnclosedWriter
```

```text
File size with the writer still open: 0 bytes
File size after close()             : 25 bytes
```

The text was written into the writer's buffer, not to the file. Until the buffer is flushed or the writer is closed, the file stays empty. If the program had ended without `close()`, the text would have been lost. Use try-with-resources and you cannot forget.

## 2. `UnhandledIOException.java`: ignoring a checked exception

```bash
javac UnhandledIOException.java
```

```text
UnhandledIOException.java:8: error: unreported exception FileNotFoundException; must be caught or declared to be thrown
        BufferedReader reader = new BufferedReader(new FileReader("employees.csv"));
                                                   ^
1 error
```

The `FileReader` constructor can throw `FileNotFoundException`, a checked exception. Either catch it, or declare `throws` on the method so the caller deals with it.

## 3. `CatchOrderIO.java`: general catch before specific

```bash
javac CatchOrderIO.java
```

```text
CatchOrderIO.java:13: error: exception FileNotFoundException has already been caught
        } catch (FileNotFoundException e) {
          ^
1 error
```

`FileNotFoundException` is a kind of `IOException`, so the first block already catches it and the second can never run. Put the specific one first.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

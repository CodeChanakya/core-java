# Break it on purpose

Each file here is a classic mistake with exceptions. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `UnhandledChecked.java`: ignoring a checked exception

```bash
javac UnhandledChecked.java
```

```text
UnhandledChecked.java:17: error: unreported exception OutOfMoneyException; must be caught or declared to be thrown
        withdraw(100, 500);
                ^
1 error
```

`withdraw` declares `throws OutOfMoneyException`, so every caller must either catch it in a `try` / `catch`, or pass the problem upward by declaring `throws` too. The compiler enforces this so a failure that can reasonably happen cannot be forgotten.

## 2. `CatchOrder.java`: the general catch first

```bash
javac CatchOrder.java
```

```text
CatchOrder.java:10: error: exception ArrayIndexOutOfBoundsException has already been caught
        } catch (ArrayIndexOutOfBoundsException e) {
          ^
1 error
```

Java tries the `catch` blocks from top to bottom. `Exception` already covers `ArrayIndexOutOfBoundsException`, so the second block can never run, and the compiler says so. Put the most specific handler first and the most general last.

## 3. `SwallowedException.java`: an empty catch block

```bash
javac SwallowedException.java
java SwallowedException
```

```text
Balance is 0
Fee is 0
```

The conversion failed, the catch block did nothing, and the program went on to print a fee calculated from a balance of 0. No error appeared anywhere. At the very least, report the problem or stop. An empty catch is the most expensive kind of silence in programming.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

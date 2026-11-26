# Break it on purpose

Each file here breaks one rule of sealed types and pattern matching. Read the compiler's message: here it is the teacher.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `NonExhaustiveSwitch.java`: a switch that misses a case

```bash
javac NonExhaustiveSwitch.java
```

```text
NonExhaustiveSwitch.java:17: error: the switch expression does not cover all possible input values
        return switch (shape) {
               ^
1 error
```

The switch covers some shapes of a sealed type but not all. The compiler lists the problem instead of letting a missing case fail at run time. Add the missing case. Do not add `default` just to silence it.

## 2. `NotPermitted.java`: extending a sealed type that does not permit you

```bash
javac NotPermitted.java
```

```text
NotPermitted.java:10: error: class is not allowed to extend sealed class: Shape (as it is not listed in its 'permits' clause)
    record Square(double side) implements Shape {
    ^
1 error
```

The sealed type names the classes allowed to extend it, and this one is not on the list. To allow it, add it to `permits`. Then expect the exhaustive switches to ask for a case.

## 3. `DominatedPattern.java`: a general case before a specific one

```bash
javac DominatedPattern.java
```

```text
DominatedPattern.java:10: error: this case label is dominated by a preceding case label
            case Book b when b.pages() > 500 -> "a long book";
                 ^
1 error
```

Java tests cases from top to bottom. A general case placed first catches everything, so the specific one below it could never run. Put the more specific case first.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

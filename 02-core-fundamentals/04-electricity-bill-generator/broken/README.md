# Break it on purpose

Each file here has one mistake in a decision or a method. Compile it, read the message (or the result) and fix the file.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `AssignmentInCondition.java`: one equals sign instead of two

```bash
javac AssignmentInCondition.java
```

```text
AssignmentInCondition.java:6: error: incompatible types: int cannot be converted to boolean
        if (units = 100) {
                  ^
1 error
```

`=` stores a value. `==` compares two values. An `if` needs a true or false answer, and `units = 100` is an assignment that produces an `int`. Write `units == 100`.

## 2. `MissingReturn.java`: a path with no return

```bash
javac MissingReturn.java
```

```text
MissingReturn.java:10: error: missing return statement
    }
    ^
1 error
```

If `units` is above 200, neither branch returns anything. Java checks every path, and refuses to compile a method that might finish without a value. Add a final `else` that returns "slab 3".

## 3. `StraySemicolon.java`: a semicolon that ends the `if` early

```bash
javac StraySemicolon.java
java StraySemicolon
```

```text
High usage warning
Units: 50
```

With `units` equal to 50 the warning should not print, but it does. The `;` after the condition is an empty statement, so the `if` controls nothing, and the braces below it are just a plain block that always runs. Delete the semicolon.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

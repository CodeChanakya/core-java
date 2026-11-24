# Break it on purpose

Each file here has one mistake about numbers. Compile it, read the message, fix the file and run it again.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `LossyConversion.java`: a decimal into an int

```bash
javac LossyConversion.java
```

```text
LossyConversion.java:5: error: incompatible types: possible lossy conversion from double to int
        int bonus = 2500.75;
                    ^
1 error
```

A `double` cannot be stored in an `int` without losing the `.75`. Java stops you rather than doing it silently. Either make `bonus` a `double`, or write `(int) 2500.75` if you really want to cut the decimals off.

## 2. `UninitialisedVariable.java`: a variable with no value

```bash
javac UninitialisedVariable.java
```

```text
UninitialisedVariable.java:6: error: variable total might not have been initialized
        System.out.println("Total: " + total);
                                       ^
1 error
```

`int total;` creates the variable but gives it no value, and Java will not let you read it. Give it a value when you declare it: `int total = 0;`.

## 3. `IntegerDivision.java`: the answer is wrong, but there is no error

```bash
javac IntegerDivision.java
java IntegerDivision
```

```text
Attendance: 0%
```

This is the most dangerous kind of mistake because Java does not complain. `26 / 30` is `0` in whole numbers. Multiply first (`26 * 100 / 30`) or use `100.0`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

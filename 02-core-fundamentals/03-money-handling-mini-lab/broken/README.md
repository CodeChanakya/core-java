# Break it on purpose

Each file here is a way money code goes wrong. Compile it, read the message (or the result), and fix the file.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `AddWithPlus.java`: no `+` for BigDecimal

```bash
javac AddWithPlus.java
```

```text
AddWithPlus.java:9: error: bad operand types for binary operator '+'
        BigDecimal total = first + second;
                                 ^
  first type:  BigDecimal
  second type: BigDecimal
1 error
```

`BigDecimal` is an object, and Java only allows `+` on numbers and Strings. Use `first.add(second)`.

## 2. `DivideWithoutScale.java`: an exception at run time

```bash
javac DivideWithoutScale.java
java DivideWithoutScale
```

```text
Exception in thread "main" java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
	at java.base/java.math.BigDecimal.divide(BigDecimal.java:1783)
	at DivideWithoutScale.main(DivideWithoutScale.java:9)
```

The file compiles, because dividing two `BigDecimal` values is allowed. It fails while running, because `1000 / 3` has no exact decimal answer. Pass a scale and a rounding mode: `amount.divide(people, 2, RoundingMode.HALF_UP)`. Read the first line of a stack trace first: it names the exception and says why.

## 3. `DoubleConstructor.java`: the value looks right but is not

```bash
javac DoubleConstructor.java
java DoubleConstructor
```

```text
Price: 19.989999999999998436805981327779591083526611328125
```

`19.99` is turned into a `double` first, and the `double` is already slightly wrong. `BigDecimal` then copies that error faithfully. Write `new BigDecimal("19.99")`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

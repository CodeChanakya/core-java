# Break it on purpose

Each file here shows one testing mistake. They need only the JDK, so you can run them without JUnit.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `JUnit4Import.java`: a JUnit 4 import in a JUnit 5 project

```bash
javac JUnit4Import.java
```

```text
JUnit4Import.java:1: error: package org.junit does not exist
import org.junit.Test;
                ^
JUnit4Import.java:6: error: cannot find symbol
    @Test
     ^
  symbol:   class Test
  location: class JUnit4Import
2 errors
```

JUnit 5 lives in `org.junit.jupiter.api`. `org.junit.Test` belongs to JUnit 4 and is not part of JUnit 5, so the compiler cannot find it. This is the most common mistake when you copy a test from an old tutorial. (The output above comes from `javac` without any jar. With the JUnit 5 jar on the class path, the same two errors appear, because the jar has no `org.junit.Test` either.) Check that every import in a test file starts with `org.junit.jupiter`.

## 2. `BigDecimalScale.java`: 90.0 is not equal to 90.00

```bash
javac BigDecimalScale.java
java BigDecimalScale
```

```text
expected = 90.0, actual = 90.00
equals()    : false   <- what assertEquals uses
compareTo() : 0       <- 0 means same value
```

`assertEquals` calls `equals`, and `equals` on `BigDecimal` compares the scale as well as the value. A failing test reads `expected: <90.0> but was: <90.00>`, which looks like nonsense until you know this. Either build expected values with the same scale as the code, as this program does, or compare with `compareTo`.

## 3. `SharedStateBetweenTests.java`: tests that depend on each other

```bash
javac SharedStateBetweenTests.java
java SharedStateBetweenTests
```

```text
order 1:
PASS  basket starts empty
PASS  basket has one item
order 2:
PASS  basket has one item
FAIL  basket starts empty
```

Both "tests" pass in the first order. In the second order the empty-basket test fails, only because the other one ran first and left a pen behind. A suite like that passes on your machine and fails on the server. JUnit gives each test a new object so that instance fields start clean, but `static` fields are shared, so keep tests free of shared mutable state.

*The output above was captured from a real run with OpenJDK 21.0.13. Other versions may word some messages a little differently.*

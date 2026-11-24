# Break it on purpose

Each file here is a classic mistake with arrays. Compile it, run it, and read the message.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `OffByOne.java`: one step too far

```bash
javac OffByOne.java
java OffByOne
```

```text
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 4
	at OffByOne.main(OffByOne.java:8)
```

The array has four marks, at positions 0 to 3. The loop condition `<=` lets `i` reach 4, which does not exist. The message names the bad index and the real length. Use `i < marks.length`. The last line tells you which line of your own code caused it: `OffByOne.java:8`.

## 2. `LengthWithBrackets.java`: brackets where there should be none

```bash
javac LengthWithBrackets.java
```

```text
LengthWithBrackets.java:6: error: cannot find symbol
        System.out.println("Subjects: " + marks.length());
                                               ^
  symbol:   method length()
  location: variable marks of type int[]
1 error
```

Arrays have a `length` field, not a `length()` method. Strings are the other way round: `name.length()` has brackets. This is one of the most common beginner slips in Java.

## 3. `MixedArrayTypes.java`: a decimal in a whole-number array

```bash
javac MixedArrayTypes.java
```

```text
MixedArrayTypes.java:5: error: incompatible types: possible lossy conversion from double to int
        int[] marks = {92, 88.5, 95, 90};
                           ^
1 error
```

Every element of an `int[]` must be a whole number. The caret points at the `88.5`. Use a `double[]` if you need decimals.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

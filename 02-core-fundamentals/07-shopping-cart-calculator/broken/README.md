# Break it on purpose

Each file here is a classic mistake with Strings. Compile it, run it, and work out what happened.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `StringEquality.java`: `==` versus `equals`

```bash
javac StringEquality.java
java StringEquality
```

```text
== says     : false
equals says: true
```

Both Strings contain `SAVE10`, but `==` says `false`. `==` asks whether the two names point to the very same object. The text typed by a user is always a different object from the text in your code, so a coupon check written with `==` would reject every customer. Use `equals`.

## 2. `IgnoredResult.java`: a result thrown away

```bash
javac IgnoredResult.java
java IgnoredResult
```

```text
[  save10 ]
```

`trim()` and `toUpperCase()` do not change `coupon`. A String can never change. Each method returns a new String, and this code drops it. The brackets in the output show the spaces are still there. Write `coupon = coupon.trim().toUpperCase();`.

## 3. `BuilderAsString.java`: a `StringBuilder` is not a `String`

```bash
javac BuilderAsString.java
```

```text
BuilderAsString.java:6: error: incompatible types: StringBuilder cannot be converted to String
        String result = text;
                        ^
1 error
```

They are different types. Ask the builder for its text with `text.toString()`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

# Break it on purpose

Each file here is a classic mistake with generics.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `PrimitiveTypeArgument.java`: a primitive as a type argument

```bash
javac PrimitiveTypeArgument.java
```

```text
PrimitiveTypeArgument.java:8: error: unexpected type
        List<int> marks = new ArrayList<>();
             ^
  required: reference
  found:    int
1 error
```

Generics work with objects, and `int` is not one. Use the wrapper class: `List<Integer>`. Java boxes and unboxes the values for you, so `marks.add(90)` still works.

## 2. `WrongType.java`: the wrong kind of item

```bash
javac WrongType.java
```

```text
WrongType.java:10: error: incompatible types: int cannot be converted to String
        names.add(42);
                  ^
Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
1 error
```

This is the compiler doing the job generics exist for. Before generics, `names.add(42)` was legal and the failure came much later. The "messages have been simplified" note is javac's own, and is harmless.

## 3. `RawTypeCrash.java`: a raw type hides the mistake

```bash
javac RawTypeCrash.java
java RawTypeCrash
```

```text
ASHA
Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String (java.lang.Integer and java.lang.String are in module java.base of loader 'bootstrap')
	at RawTypeCrash.main(RawTypeCrash.java:14)
```

The first item printed fine. The second was an `Integer` that got into a list the compiler was no longer watching, and the cast failed at run time. The `@SuppressWarnings` line only silences the compiler's complaint; the problem is still there. Always write `List<String>`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

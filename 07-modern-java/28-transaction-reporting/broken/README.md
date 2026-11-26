# Break it on purpose

Each file here breaks one rule of records and `Optional`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `RecordSetter.java`: a setter on a record

```bash
javac RecordSetter.java
```

```text
RecordSetter.java:9: error: cannot assign a value to final variable x
        point.x = 10;
             ^
1 error
```

Record fields are final, so there is nothing to set. If you need a changed copy, write a method that returns a new record, like `withAmount`.

## 2. `CompactConstructorAssign.java`: assigning to the field in a compact constructor

```bash
javac CompactConstructorAssign.java
```

```text
CompactConstructorAssign.java:9: error: cannot assign a value to final variable amount
            this.amount = amount;
                ^
1 error
```

In a compact constructor the fields are assigned for you at the end. To change a value, reassign the *parameter* (`amount = amount.setScale(2);`), not `this.amount`.

## 3. `OptionalGetEmpty.java`: calling get() on an empty Optional

```bash
javac OptionalGetEmpty.java
java OptionalGetEmpty
```

```text
Is there a match? false
Exception in thread "main" java.util.NoSuchElementException: No value present
	at java.base/java.util.Optional.get(Optional.java:143)
	at OptionalGetEmpty.main(OptionalGetEmpty.java:11)
```

`get()` has no safe path: it throws when there is nothing. Use `orElse`, `ifPresent` or `orElseThrow` with a message that explains the problem.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

# Break it on purpose

Each file here is a classic mistake with enums and `static`. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `EnumTypo.java`: a misspelt constant

```bash
javac EnumTypo.java
```

```text
EnumTypo.java:7: error: cannot find symbol
        Status status = Status.PAYED;
                              ^
  symbol:   variable PAYED
  location: class Status
1 error
```

With Strings this slipped through silently. With an enum the compiler checks every use against the declared list and stops on the exact line. This one error is the main reason to prefer an enum over a String for a fixed set of values.

## 2. `MissingSwitchCase.java`: a switch that forgot a case

```bash
javac MissingSwitchCase.java
```

```text
MissingSwitchCase.java:8: error: the switch expression does not cover all possible input values
        String text = switch (status) {
                      ^
1 error
```

`SHIPPED` has no branch. A switch expression has to produce a value for every input, so the compiler refuses to guess. Add the missing case. Resist adding `default`, which would hide the same problem the next time a constant is added.

## 3. `StaticFromInstance.java`: a static method reading an instance field

```bash
javac StaticFromInstance.java
```

```text
StaticFromInstance.java:7: error: non-static variable customer cannot be referenced from a static context
        System.out.println("Customer: " + customer);
                                          ^
1 error
```

`printCustomer` is static, so it belongs to the class and is not tied to any object. `customer` belongs to one particular object. Java asks which one you mean. Make the method non-static, or pass the value in as a parameter.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

# Break it on purpose

Each file here is a classic mistake with interfaces and composition.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ConcreteOnlyMethod.java`: calling beyond the interface

```bash
javac ConcreteOnlyMethod.java
```

```text
ConcreteOnlyMethod.java:6: error: cannot find symbol
        System.out.println("Limit left: " + method.remainingLimit());
                                                  ^
  symbol:   method remainingLimit()
  location: variable method of type PaymentMethod
1 error
```

The variable is typed as the interface, so only the interface's methods are visible, whatever object it holds. If the caller needs `remainingLimit()`, either the interface should offer it (if every method of payment has one) or the caller should deliberately work with `CardPayment`.

## 2. `MissingComponent.java`: a part nobody plugged in

```bash
javac MissingComponent.java
java MissingComponent
```

```text
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "PaymentMethod.charge(double)" because "this.method" is null
	at PaymentProcessor.pay(MissingComponent.java:18)
	at MissingComponent.main(MissingComponent.java:6)
```

The field `method` was never given a value, so it is `null`. Composition moves the work of assembly to whoever builds the object. Take the part in the constructor (as `PaymentProcessor` does in the main code) so that an incomplete processor cannot be created at all.

## 3. `AssigningFinalField.java`: changing something that was meant to be fixed

```bash
javac AssigningFinalField.java
```

```text
AssigningFinalField.java:19: error: cannot assign a value to final variable approved
        approved = true;
        ^
1 error
```

`final` fields are set exactly once, in the constructor. That is what makes a class like `PaymentResult` safe to pass around. If a value needs to change, it should not be `final`; if it should not change, the method must go.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

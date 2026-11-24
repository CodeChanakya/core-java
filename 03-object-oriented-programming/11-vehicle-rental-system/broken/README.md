# Break it on purpose

Each file here is a classic mistake with abstract classes and interfaces.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `InstantiateAbstract.java`: creating the template itself

```bash
javac InstantiateAbstract.java
```

```text
InstantiateAbstract.java:5: error: Vehicle is abstract; cannot be instantiated
        Vehicle vehicle = new Vehicle();
                          ^
1 error
```

`Vehicle` has an abstract method, so an object of exactly that type would have no way to answer `dailyRate()`. Create a subclass and build that instead.

## 2. `MissingAbstractMethod.java`: an unfinished subclass

```bash
javac MissingAbstractMethod.java
```

```text
MissingAbstractMethod.java:16: error: Truck is not abstract and does not override abstract method kind() in Vehicle
class Truck extends Vehicle {
^
1 error
```

`Vehicle` promised two methods. `Truck` supplied `dailyRate()` but not `kind()`. Java reports the first missing one it finds; fix it and the next, if any, appears. Add the method, or mark `Truck` itself `abstract`.

## 3. `WeakerAccess.java`: an interface method that is not public

```bash
javac WeakerAccess.java
```

```text
WeakerAccess.java:15: error: insurancePerDay() in Car cannot implement insurancePerDay() in Insurable
    double insurancePerDay() {
           ^
  attempting to assign weaker access privileges; was public
1 error
```

Every method in an interface is implicitly `public`. An implementation may not be *less* visible than what it promises. Add the word `public`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

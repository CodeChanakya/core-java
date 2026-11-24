# Break it on purpose

Each file here is a classic mistake with inheritance. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `MissingSuperCall.java`: a parent that needs arguments

```bash
javac MissingSuperCall.java
```

```text
MissingSuperCall.java:21: error: constructor Employee in class Employee cannot be applied to given types;
    Developer(String name, String language) {
                                            ^
  required: String
  found:    no arguments
  reason: actual and formal argument lists differ in length
1 error
```

`Employee` only has a constructor that takes a name, so Java cannot build the parent part of a `Developer` by itself. Every child constructor must start with `super(name);` to say how. Without any constructor in the parent, Java would add an empty one for you; the moment you write your own, that free one is gone.

## 2. `OverrideTypo.java`: @Override catches a misspelling

```bash
javac OverrideTypo.java
```

```text
OverrideTypo.java:17: error: method does not override or implement a method from a supertype
    @Override
    ^
1 error
```

`monthlypay` with a lowercase `p` is a different method name, so nothing is being overridden. `@Override` turned a silent bug into an error. Delete the annotation and the code compiles, and the manager is paid the parent's figure. Always write `@Override`.

## 3. `ParentTypeLimits.java`: the variable decides what you may call

```bash
javac ParentTypeLimits.java
```

```text
ParentTypeLimits.java:6: error: cannot find symbol
        System.out.println("Team size: " + boss.getTeamSize());
                                               ^
  symbol:   method getTeamSize()
  location: variable boss of type Employee
1 error
```

The object is a `Manager`, but the variable `boss` is an `Employee`, and the compiler only trusts the variable. Either declare `Manager boss`, or test with `if (boss instanceof Manager m)` and use `m`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

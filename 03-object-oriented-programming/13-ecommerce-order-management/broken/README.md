# Break it on purpose

Each file here is a classic mistake in the way objects relate. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `StatusTypo.java`: a rule that never fires

```bash
javac StatusTypo.java
java StatusTypo
```

```text
Cannot ship: order is PAID
```

There is no error anywhere. `"PAYED"` is simply never equal to `"PAID"`, so the shipping branch is dead code. This is the weakness of using Strings for a fixed set of values. Program 14 replaces them with an enum, where a typo does not compile.

## 2. `SharedListBug.java`: two objects sharing one list

```bash
javac SharedListBug.java
java SharedListBug
```

```text
ORD-1 has [Laptop Stand, Java Ebook]
ORD-2 has [Laptop Stand, Java Ebook]
```

Both orders were given the same `ArrayList`, so they are looking at the same items. If an object needs its own list, it must create it: `this.items = new ArrayList<>();`, as `Order` does. Passing a mutable list in from outside is how this bug gets in.

## 3. `UncheckedStock.java`: no check before changing state

```bash
javac UncheckedStock.java
java UncheckedStock
```

```text
Mechanical Keyboard stock: -2
```

`removeStock` subtracts blindly, so the shelf holds minus two keyboards. The real `Product.removeStock` checks `hasStock` first and `OrderService` checks before it calls. Make the class protect its own rule; do not rely on every caller remembering.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

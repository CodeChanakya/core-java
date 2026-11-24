# 16 · Generic Repository

**Phase 04 · Java Essentials** &nbsp;|&nbsp; **Level:** Intermediate to advanced &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: generic classes and interfaces, type parameters, bounded types (`extends`), generic methods, wildcards, `Comparable`, why raw types are a trap.

---

## The problem

The shop needs to store customers. Then products. Then orders. Each time, the code is the same: save one, find by id, list all, delete. Copy-pasting `CustomerStore`, `ProductStore` and `OrderStore` gives three classes that must be kept in step, and each new type adds another.

Before generics, the alternative was one store holding `Object`, and a cast on every read: `(Customer) store.get("C-1")`. If someone had saved a product there, you found out when the program crashed.

Generics give you one repository written once, for **any** type, with the compiler checking you never mix them up.

## Requirements

**Functional**
1. Save, find by id, list, delete and count, for any item that has an id.
2. The id can be a `String` for customers and an `int` for products.
3. Saving an item with an existing id replaces it.
4. A few generic helpers: the largest item, a swap, a sum, a printer.

**Non-functional**
- Putting a product into the customer repository must not compile.
- No casts anywhere.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`Identifiable.java`](src/main/java/com/codechanakya/repository/Identifiable.java) | One type parameter, `ID`. |
| 2 | [`Repository.java`](src/main/java/com/codechanakya/repository/Repository.java) | Two parameters, and the bound `T extends Identifiable<ID>`. |
| 3 | [`InMemoryRepository.java`](src/main/java/com/codechanakya/repository/InMemoryRepository.java) | The one real implementation. |
| 4 | [`Customer.java`](src/main/java/com/codechanakya/repository/Customer.java), [`Product.java`](src/main/java/com/codechanakya/repository/Product.java) | Two entities with two kinds of id. |
| 5 | [`Util.java`](src/main/java/com/codechanakya/repository/Util.java) | Generic methods and wildcards. |
| 6 | [`Pair.java`](src/main/java/com/codechanakya/repository/Pair.java) | A generic class with two independent parameters. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Expected output:

```text
Customers (3)
  C-101 Asha Menon (Pune)
  C-102 Rahul Verma (Mumbai)
  C-103 Meera Iyer (Chennai)

Products (3)
  1 Laptop Stand 1299.00
  2 Mechanical Keyboard 3499.00
  3 Wireless Mouse 899.00

--- Finding and deleting ---
Customer C-102: C-102 Rahul Verma (Mumbai)
Customer C-999: null
Product 3: 3 Wireless Mouse 899.00
Delete product 2: true
Delete product 2 again: false
Products left: 2

--- Saving with an existing id replaces ---
C-101 is now: C-101 Asha Menon (Bengaluru)
Customers still: 3

--- Bounded generic methods ---
Most expensive product: 1 Laptop Stand 1299.00
Last name alphabetically: Rahul
After swap: third, second, first
Sum of counts: 27.0
Sum of weights: 3.75
A pair: (Laptop Stand, 12), name length 12, doubled count 24
```

## Reading the angle brackets

| You see | Read it as |
|---|---|
| `List<String>` | a list of Strings |
| `Repository<Customer, String>` | a repository of Customers, found by String ids |
| `class Pair<A, B>` | A and B are placeholders, filled in when someone uses `Pair` |
| `<T extends Comparable<T>>` | T can be any type that knows how to compare itself with its own kind |
| `List<?>` | a list of something; I do not care what |
| `List<? extends Number>` | a list of Numbers or any subtype of Number |

By convention, `T` is a type, `E` an element, `K` and `V` a key and value, `ID` an identifier. They are ordinary names; any identifier would work.

## The key idea: write once, type-check forever

```java
Repository<Customer, String> customers = new InMemoryRepository<>();
Repository<Product, Integer> products = new InMemoryRepository<>();

customers.save(new Customer(...));    // fine
customers.save(new Product(...));     // compile error
Customer c = customers.findById("C-101");   // no cast needed
```

The compiler substitutes the real types for `T` and `ID`, and rejects anything that does not fit. The mistake that used to be a crash at run time is now a red line in the editor.

## Bounds

```java
public interface Repository<T extends Identifiable<ID>, ID>
```

Without the bound, `T` could be anything, and `item.getId()` would not compile because the compiler cannot know a `T` has such a method. The bound promises it: every `T` here is `Identifiable<ID>`. A bound is how you tell the compiler what a type parameter can do.

```java
public static <T extends Comparable<T>> T maxOf(List<T> items)
```

Here the bound lets the method call `compareTo`. Try it with a list of `Customer` (which has no ordering) and the compiler refuses, which is right.

## Generic methods

A method can have its own type parameter, written before the return type:

```java
public static <T> void swap(T[] array, int i, int j)
```

You never write `Util.<String>swap(...)`; the compiler works `T` out from the argument.

## Wildcards

`List<Integer>` is **not** a `List<Number>`, even though `Integer` is a `Number`. If it were, you could put a `Double` into a list of Integers through the `Number` view. A wildcard says what you need without that risk:

```java
double sum(List<? extends Number> numbers)   // reads Numbers; works for Integer and Double lists
void printAll(List<?> items)                  // reads Objects; works for anything
```

Rule of thumb: `? extends T` when you only **read** from it. Wildcards are the part of generics that takes time to feel natural; read the two methods in `Util` until they do.

## Generics and primitives

Type arguments must be objects. `List<int>` does not compile; write `List<Integer>`. Java converts between `int` and `Integer` automatically (boxing), which is why `stock.getSecond() * 2` works in `Main`.

## Raw types: do not

```java
List names = new ArrayList();     // no <...>: a "raw" type
names.add(42);                    // compiles
String s = (String) names.get(0); // fails at run time
```

A raw type switches the checking off and moves the failure from compile time to run time. It exists for old code written before generics. Always write the type.

## Common mistakes

| Mistake | What you see |
|---|---|
| `List<int>` | `unexpected type; required: reference, found: int` |
| Adding the wrong type to a typed list | `incompatible types` |
| A raw `List` | `ClassCastException` later, at run time |
| Forgetting the bound and calling a method on `T` | `cannot find symbol` |

Try three of these in [`broken/`](broken/README.md).

## Practice

- [`exercises/`](exercises/README.md): six tasks, three beginner, two intermediate, one challenge.
- [`solutions/`](solutions/README.md): one answer to each.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 17 is the Phase 4 project. It uses enums, exceptions, a `Comparable` type and annotations to build a small plugin system that finds its own plugins at run time.

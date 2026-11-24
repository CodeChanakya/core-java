# Exercises: Generic Repository

Work in a copy so the original stays intact:

```bash
cp -r 16-generic-repository ../my-generic-repository
cd ../my-generic-repository
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Save a fourth customer and fetch them by id.

<details><summary>Hint</summary>`customers.save(new Customer("C-104", ...))`.</details>

**B2.** Add a `default` method `exists(ID)` to the `Repository` interface. Do you need to change `InMemoryRepository`?

<details><summary>Hint</summary>No: a default method gives every implementer the code for free. It can call `findById`.</details>

**B3.** Print how many customers there are.

<details><summary>Hint</summary>`customers.count()`.</details>

## Intermediate

**I1.** Add `minOf` to `Util`, with the same bound as `maxOf`.

<details><summary>Hint</summary>Copy `maxOf` and flip the comparison.</details>

**I2.** Add `sorted(List<T>)` to `Util` that returns a sorted copy and leaves the original alone.

<details><summary>Hint</summary>`Collections.sort(copy)` needs the elements to be `Comparable`, which is exactly the bound you need.</details>

## Challenge

**C1.** Write a generic `Cache<K, V>` with a fixed capacity. When full, adding one more pushes out the oldest entry.

<details><summary>Hint 1</summary>A `LinkedHashMap` remembers insertion order.</details>
<details><summary>Hint 2</summary>The oldest key is `map.keySet().iterator().next()`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

# Break it on purpose

Each file here is a classic mistake with lists. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ConcurrentRemove.java`: removing while looping

```bash
javac ConcurrentRemove.java
java ConcurrentRemove
```

```text
Exception in thread "main" java.util.ConcurrentModificationException
	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1095)
	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:1049)
	at ConcurrentRemove.main(ConcurrentRemove.java:9)
```

The loop was in the middle of walking the list when the list shrank under it. Java detects the mismatch and stops instead of skipping elements without telling you. Use an `Iterator` and its `remove()`, or build a new list of what to keep.

## 2. `ImmutableListAdd.java`: changing a list that cannot change

```bash
javac ImmutableListAdd.java
java ImmutableListAdd
```

```text
Before: [Mon, Tue, Wed]
Exception in thread "main" java.lang.UnsupportedOperationException
	at java.base/java.util.ImmutableCollections.uoe(ImmutableCollections.java:142)
	at java.base/java.util.ImmutableCollections$AbstractImmutableCollection.add(ImmutableCollections.java:147)
	at ImmutableListAdd.main(ImmutableListAdd.java:9)
```

`List.of(...)` returns a list that refuses every change. This is a feature: nobody can alter it by accident. If you need to add, copy it first: `new ArrayList<>(List.of(...))`.

## 3. `SortWithoutComparator.java`: sorting items with no order

```bash
javac SortWithoutComparator.java
```

```text
SortWithoutComparator.java:19: error: no suitable method found for sort(List<Item>)
        Collections.sort(items);
                   ^
    method Collections.<T#1>sort(List<T#1>) is not applicable
      (inference variable T#1 has incompatible bounds
        equality constraints: Item
        upper bounds: Comparable<? super T#1>)
    method Collections.<T#2>sort(List<T#2>,Comparator<? super T#2>) is not applicable
      (cannot infer type-variable(s) T#2
        (actual and formal argument lists differ in length))
  where T#1,T#2 are type-variables:
    T#1 extends Comparable<? super T#1> declared in method <T#1>sort(List<T#1>)
    T#2 extends Object declared in method <T#2>sort(List<T#2>,Comparator<? super T#2>)
1 error
```

`Collections.sort(list)` only works when the item type implements `Comparable`. `Item` does not, so the compiler rejects the call and explains what is missing. Either make the class `Comparable` or pass a `Comparator`: `items.sort(Comparator.comparing(...))`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

# Break it on purpose

Each file here is a classic mistake with maps.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `WrongValueType.java`: the wrong kind of value

```bash
javac WrongValueType.java
```

```text
WrongValueType.java:19: error: incompatible types: Product cannot be converted to List<Product>
        groups.put(Category.HOME, new Product("Desk Lamp"));
                                  ^
Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
1 error
```

The map's type says every value is a `List<Product>`. The compiler checks every `put` against that promise, which is the whole benefit of generics. Put the product in a list first, or use `MapUtil.addToGroup`.

## 2. `TreeMapNullKey.java`: a null key in a sorted map

```bash
javac TreeMapNullKey.java
java TreeMapNullKey
```

```text
HashMap accepts a null key: {null=1}
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.lang.Comparable.compareTo(Object)" because "k1" is null
	at java.base/java.util.TreeMap.compare(TreeMap.java:1604)
	at java.base/java.util.TreeMap.addEntryToEmptyMap(TreeMap.java:811)
	at java.base/java.util.TreeMap.put(TreeMap.java:820)
	at java.base/java.util.TreeMap.put(TreeMap.java:569)
```

`HashMap` allows one `null` key, so the first line works. `TreeMap` has to compare keys to keep them in order, and `null` cannot be compared. Avoid `null` keys everywhere; they cause surprises like this.

## 3. `ModifyMapWhileLooping.java`: removing while looping over a map

```bash
javac ModifyMapWhileLooping.java
java ModifyMapWhileLooping
```

```text
Exception in thread "main" java.util.ConcurrentModificationException
	at java.base/java.util.LinkedHashMap$LinkedHashIterator.nextNode(LinkedHashMap.java:1023)
	at java.base/java.util.LinkedHashMap$LinkedKeyIterator.next(LinkedHashMap.java:1046)
	at ModifyMapWhileLooping.main(ModifyMapWhileLooping.java:14)
```

The same problem as removing from a list inside a for-each. Remove through an iterator over `entrySet()`, or call `stock.values().removeIf(...)` (Phase 7), or build a new map of what to keep.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

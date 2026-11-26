# Break it on purpose

Each file here is a classic mistake with sets.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `TreeSetNotComparable.java`: sorting what cannot be sorted

```bash
javac TreeSetNotComparable.java
java TreeSetNotComparable
```

```text
Adding the first person...
Exception in thread "main" java.lang.ClassCastException: class TreeSetNotComparable$Person cannot be cast to class java.lang.Comparable (TreeSetNotComparable$Person is in unnamed module of loader 'app'; java.lang.Comparable is in module java.base of loader 'bootstrap')
	at java.base/java.util.TreeMap.compare(TreeMap.java:1604)
	at java.base/java.util.TreeMap.addEntryToEmptyMap(TreeMap.java:811)
	at java.base/java.util.TreeMap.put(TreeMap.java:820)
	at java.base/java.util.TreeMap.put(TreeMap.java:569)
```

A `TreeSet` must put things in order, so the first `add` needs to compare. `Person` has no order, and Java finds out only when the code runs. Implement `Comparable<Person>`, or give the `TreeSet` a `Comparator` when you create it.

## 2. `SetIndexAccess.java`: asking a Set for a position

```bash
javac SetIndexAccess.java
```

```text
SetIndexAccess.java:11: error: cannot find symbol
        String first = cities.get(0);
                             ^
  symbol:   method get(int)
  location: variable cities of type Set<String>
1 error
```

A set has no first or second item, so it has no `get(int)`. If you need the "first" one, you want a `List` or a `LinkedHashSet` (iterate once and stop), or a `TreeSet` (`first()`).

## 3. `CompareToTooBroad.java`: two people treated as one

```bash
javac CompareToTooBroad.java
java CompareToTooBroad
```

```text
Two different people added, set holds: 1
```

No error, and no warning. `compareTo` said the two customers are equal because their names match, so the second never made it into the set. This is the most dangerous kind of bug here: lost data with a normal-looking result. Include every field that makes a record unique.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

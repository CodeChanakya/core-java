# Break it on purpose

Each file here is a classic mistake with maps and hashing.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `EqualsWithoutHashCode.java`: half of the contract

```bash
javac EqualsWithoutHashCode.java
java EqualsWithoutHashCode
```

```text
a.equals(b): true
Set size: 2
Contains a copy of a: false
```

`equals` says the two employees are the same, yet the set holds both and cannot find a copy. They were put in different buckets, because `hashCode` was inherited and is based on the object's identity. Always override the two together.

## 2. `MutableKey.java`: a key that changes

```bash
javac MutableKey.java
java MutableKey
```

```text
Before: Asha
After : null
Size  : 1
```

The badge was stored under the hash of `B-1`. After the edit it hashes as `B-2`, so the lookup goes to the wrong bucket and finds nothing. The entry is still inside the map: the size is 1. Keep keys immutable.

## 3. `UnboxNullFromMap.java`: a missing key becomes null

```bash
javac UnboxNullFromMap.java
java UnboxNullFromMap
```

```text
Pens: 20
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()" because the return value of "java.util.Map.get(Object)" is null
	at UnboxNullFromMap.main(UnboxNullFromMap.java:11)
```

`get` returns `null` for a key that is not there, and Java cannot turn `null` into an `int`. Use `getOrDefault(key, 0)`, or check `containsKey` first.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

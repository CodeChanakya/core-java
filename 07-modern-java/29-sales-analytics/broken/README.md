# Break it on purpose

Each file here breaks one rule of collectors.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `DuplicateKeyToMap.java`: toMap with a repeated key

```bash
javac DuplicateKeyToMap.java
java DuplicateKeyToMap
```

```text
Exception in thread "main" java.lang.IllegalStateException: Duplicate key Asha (attempted merging values 3 and 5)
	at java.base/java.util.stream.Collectors.duplicateKeyException(Collectors.java:135)
	at java.base/java.util.stream.Collectors.lambda$uniqKeysMapAccumulator$1(Collectors.java:182)
	at java.base/java.util.stream.ReduceOps$3ReducingSink.accept(ReduceOps.java:169)
	at java.base/java.util.AbstractList$RandomAccessSpliterator.forEachRemaining(AbstractList.java:722)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
```

Two sales have the same salesperson, and `toMap` was not told what to do with two values for one key, so it fails. Pass a merge function as the third argument (`BigDecimal::add`, say), or group instead.

## 2. `NullGroupingKey.java`: grouping on a null key

```bash
javac NullGroupingKey.java
java NullGroupingKey
```

```text
Exception in thread "main" java.lang.NullPointerException: element cannot be mapped to a null key
	at java.base/java.util.Objects.requireNonNull(Objects.java:259)
	at java.base/java.util.stream.Collectors.lambda$groupingBy$53(Collectors.java:1105)
	at java.base/java.util.stream.ReduceOps$3ReducingSink.accept(ReduceOps.java:169)
	at java.base/java.util.AbstractList$RandomAccessSpliterator.forEachRemaining(AbstractList.java:722)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
```

`groupingBy` does not accept a `null` key. Give the missing value a name first (`Optional.ofNullable(x).orElse("unknown")`) or filter the row out.

## 3. `WrongResultType.java`: declaring the wrong result type

```bash
javac WrongResultType.java
```

```text
WrongResultType.java:11: error: incompatible types: inference variable D has incompatible equality constraints Integer,Long
                .collect(Collectors.groupingBy(city -> city, Collectors.counting()));
                        ^
  where D,T,K,A are type-variables:
    D extends Object declared in method <T,K,A,D>groupingBy(Function<? super T,? extends K>,Collector<? super T,A,D>)
    T extends Object declared in method <T,K,A,D>groupingBy(Function<? super T,? extends K>,Collector<? super T,A,D>)
    K extends Object declared in method <T,K,A,D>groupingBy(Function<? super T,? extends K>,Collector<? super T,A,D>)
    A extends Object declared in method <T,K,A,D>groupingBy(Function<? super T,? extends K>,Collector<? super T,A,D>)
1 error
```

`counting()` produces `Long`. The declared map says `Integer`, so the types cannot line up. Declare `Map<String, Long>`, or use `summingInt` if you want an `Integer`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

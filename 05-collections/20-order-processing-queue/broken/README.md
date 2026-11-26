# Break it on purpose

Each file here is a classic mistake with queues.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `QueueRemoveEmpty.java`: taking from an empty queue

```bash
javac QueueRemoveEmpty.java
java QueueRemoveEmpty
```

```text
poll gives: null
Exception in thread "main" java.util.NoSuchElementException
	at java.base/java.util.ArrayDeque.removeFirst(ArrayDeque.java:361)
	at java.base/java.util.ArrayDeque.remove(ArrayDeque.java:522)
	at QueueRemoveEmpty.main(QueueRemoveEmpty.java:10)
```

`poll()` reports an empty queue by returning `null`. `remove()` treats it as a bug and throws. Choose the one that matches what "empty" means in your program.

## 2. `PriorityQueueOrder.java`: trusting the printed order

```bash
javac PriorityQueueOrder.java
java PriorityQueueOrder
```

```text
Printed whole: [1, 2, 4, 5, 3]
Taken out    : 1 2 3 4 5
```

Nothing is wrong. A `PriorityQueue` keeps its contents in a heap, an arrangement that only guarantees the **smallest** item is at the front. The sorted order is produced one item at a time by `poll()`. If you need a sorted list, use `poll` in a loop or sort a copy.

## 3. `QueuePushMissing.java`: a stack call on a Queue

```bash
javac QueuePushMissing.java
```

```text
QueuePushMissing.java:9: error: cannot find symbol
        line.push("first");
            ^
  symbol:   method push(String)
  location: variable line of type Queue<String>
1 error
```

`push` is part of `Deque`, not `Queue`. The variable's declared type decides which methods exist. Declare it as `Deque<String>` if you want both ends.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

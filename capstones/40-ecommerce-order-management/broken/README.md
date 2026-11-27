# Break it on purpose

Compile and run each file from this folder (after one `./run.sh` the Derby jars are in `../lib`):

```
javac --release 21 -d /tmp/out StockCheckThenUpdate.java
java -cp "/tmp/out:../lib/derby-10.16.1.1.jar:../lib/derbyshared-10.16.1.1.jar" StockCheckThenUpdate
```

`StreamsReuse` and `MutableOrderList` need no jars.

## StockCheckThenUpdate

Two buyers look at the stock, both see 1, both buy.

```
Buyer A sees stock 1, buyer B sees stock 1
Buyer A got the item
Buyer B got the item
Stock is now -1 (it should never be below 0)
```

Between the look and the buy, the picture went out of date. The fix is one statement that checks and changes
together, `UPDATE ... SET stock = stock - ? WHERE sku = ? AND stock >= ?`, then looking at how many rows
changed. A CHECK rule on the column is the second line of defence, and the real table has one.

## StreamsReuse

```
How many: 3
Second use failed: stream has already been operated upon or closed
```

A stream is a pipe, not a list. Once a final step such as `count()` has run, it is used up. Keep the `List`,
and make a new stream each time.

## MutableOrderList

```
Placed: Order[id=1, lines=[BK-101 x2]]
Later: Order[id=1, lines=[BK-101 x2, EL-202 x5]]
Nobody called a method on the order, yet it changed.
```

A record is shallow: its fields cannot be reassigned, but a list inside it can still be changed by anyone who
holds it. The real `Order` copies the list with `List.copyOf` in its constructor.

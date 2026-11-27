# Break it on purpose

Each file needs only the JDK:

```
javac --release 21 -d /tmp/out RemoveWhileLooping.java
java -cp /tmp/out RemoveWhileLooping
```

These are three mistakes that a library system invites. Each is small, and each one has cost somebody an evening.

## RemoveWhileLooping

```
Caught: ConcurrentModificationException
The queue is now: [Bilal, Chitra]
With removeIf:    [Bilal, Chitra]
```

When Asha borrows the book, she leaves the reservation queue. Removing her inside a for-each loop over the same
list makes Java throw `ConcurrentModificationException`. (Remove the second-to-last item and the loop quietly
ends early instead, which is worse.) Use `removeIf`, or build a new list.

## StringEquality

```
stored == typed:      false
stored.equals(typed): true
```

`==` asks whether two names point at the same object. Text read from a file or a form is a different object from
a literal with the same characters, so ISBNs and member ids must be compared with `equals`.

## ReversedDates

```
Days late (wrong way round): -3
Fine at 2.00 a day:          -6.0
Days late (right way round): 3
```

`ChronoUnit.DAYS.between(start, end)` is negative when `end` comes first. A member who returns a book three days
late would be paid six rupees for it. Say the question aloud: "days from the due date to the return date".

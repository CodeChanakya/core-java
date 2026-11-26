# Break it on purpose

Each file here is a classic mistake with `java.time`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ImmutableDate.java`: changing a date that cannot change

```bash
javac ImmutableDate.java
java ImmutableDate
```

```text
Due in a week: 2026-03-06
Extended     : 2026-03-13
```

`plusDays(7)` returned a new date, and the program threw it away. Assign the result. The same is true for `minusMonths`, `withDayOfMonth` and every other method that looks like a change.

## 2. `WrongPatternLetters.java`: mm is minutes, not the month

```bash
javac WrongPatternLetters.java
java WrongPatternLetters
```

```text
05/03/2026
Exception in thread "main" java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: MinuteOfHour
	at java.base/java.time.LocalDate.get0(LocalDate.java:698)
	at java.base/java.time.LocalDate.getLong(LocalDate.java:678)
	at java.base/java.time.format.DateTimePrintContext.getValue(DateTimePrintContext.java:308)
	at java.base/java.time.format.DateTimeFormatterBuilder$NumberPrinterParser.format(DateTimeFormatterBuilder.java:2914)
```

The first pattern worked. The second asked a date for its minute of the hour, which a date does not have. A `LocalDate` has no time of day, so Java refuses. Use `MM` for the month and `mm` for minutes.

## 3. `NewLocalDate.java`: constructing a date with new

```bash
javac NewLocalDate.java
```

```text
NewLocalDate.java:7: error: LocalDate(int,int,int) has private access in LocalDate
        LocalDate date = new LocalDate(2026, 3, 5);
                         ^
1 error
```

The constructor is private on purpose: dates are made with named factory methods that say what they do. Use `LocalDate.of(2026, 3, 5)`, `LocalDate.parse(...)` or `LocalDate.now()`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

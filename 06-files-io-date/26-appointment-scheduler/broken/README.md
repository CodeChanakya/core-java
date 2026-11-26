# Break it on purpose

Each file here is a classic mistake with time zones and `java.time`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `EqualsVsIsEqual.java`: same moment, different answers

```bash
javac EqualsVsIsEqual.java
java EqualsVsIsEqual
```

```text
New York: 2026-03-09T10:00-04:00[America/New_York]
London  : 2026-03-09T14:00Z[Europe/London]
equals()  : false
isEqual() : true
Local times say New York is earlier: true
```

These are one moment on two clocks. `equals()` compares everything, zone included, so it says false. `isEqual()` compares only the instant. Comparing the local clock readings gives a third, misleading answer. For anything about order or clashes, compare instants.

## 2. `UnknownZone.java`: a zone that is not a region

```bash
javac UnknownZone.java
java UnknownZone
```

```text
Good: Asia/Kolkata
Exception in thread "main" java.time.zone.ZoneRulesException: Unknown time-zone ID: India/Mumbai
	at java.base/java.time.zone.ZoneRulesProvider.getProvider(ZoneRulesProvider.java:281)
	at java.base/java.time.zone.ZoneRulesProvider.getRules(ZoneRulesProvider.java:236)
	at java.base/java.time.ZoneRegion.ofId(ZoneRegion.java:121)
	at java.base/java.time.ZoneId.of(ZoneId.java:411)
```

Zone ids come from a fixed database of region names. There is no `India/Mumbai`; the id is `Asia/Kolkata`. Catch `DateTimeException` if the id comes from user input.

## 3. `DurationBetweenDates.java`: Duration needs exact time

```bash
javac DurationBetweenDates.java
java DurationBetweenDates
```

```text
Working out the gap...
Exception in thread "main" java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Seconds
	at java.base/java.time.LocalDate.until(LocalDate.java:1636)
	at java.base/java.time.Duration.between(Duration.java:492)
	at DurationBetweenDates.main(DurationBetweenDates.java:11)
```

A `Duration` counts seconds. A `LocalDate` has none, so the library refuses to guess. For dates use `ChronoUnit.DAYS.between(start, end)` or `Period.between(start, end)`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*

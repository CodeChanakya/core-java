import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** BROKEN: "one day later" and "24 hours later" are not the same on the day the clocks change. */
public class DurationVersusPeriod {
    public static void main(String[] args) {
        ZonedDateTime before = ZonedDateTime.of(2025, 3, 8, 12, 0, 0, 0, ZoneId.of("America/New_York"));
        System.out.println("Start:             " + before.toLocalDateTime());
        System.out.println("Plus 24 hours:     " + before.plus(Duration.ofHours(24)).toLocalDateTime());
        System.out.println("Plus 1 day:        " + before.plus(Period.ofDays(1)).toLocalDateTime());
    }
}

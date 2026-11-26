import java.time.Duration;
import java.time.LocalDate;

/** Duration measures exact time (hours, minutes, seconds). A date has none of those. */
public class DurationBetweenDates {

    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2026, 3, 2);
        LocalDate end = LocalDate.of(2026, 3, 6);
        System.out.println("Working out the gap...");
        Duration gap = Duration.between(start, end);
        System.out.println("Gap: " + gap);
    }
}

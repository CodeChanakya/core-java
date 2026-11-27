import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** BROKEN: 2025 is not a leap year, so 29 February 2025 does not exist. */
public class InvalidDate {
    public static void main(String[] args) {
        try {
            LocalDate.of(2025, 2, 29);
        } catch (java.time.DateTimeException e) {
            System.out.println("LocalDate.of(2025, 2, 29) failed: " + e.getMessage());
        }
        try {
            LocalDate.parse("2025-13-01");
        } catch (DateTimeParseException e) {
            System.out.println("parse(\"2025-13-01\") failed: " + e.getMessage());
        }
        System.out.println("A leap-day birthday in 2025 becomes: " + LocalDate.of(2024, 2, 29).plusYears(1));
    }
}

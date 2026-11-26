import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** In a date pattern, MM is the month and mm is the minute. */
public class WrongPatternLetters {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2026, 3, 5);
        System.out.println(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println(date.format(DateTimeFormatter.ofPattern("dd/mm/yyyy")));
    }
}

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** BROKEN: the arguments of between() are start, then end. Swap them and the answer changes sign. */
public class ReversedDates {
    public static void main(String[] args) {
        LocalDate due = LocalDate.of(2025, 7, 14);
        LocalDate returned = LocalDate.of(2025, 7, 17);
        long daysLate = ChronoUnit.DAYS.between(returned, due);        // wrong way round
        System.out.println("Days late (wrong way round): " + daysLate);
        System.out.println("Fine at 2.00 a day:          " + (daysLate * 2.00));
        System.out.println("Days late (right way round): " + ChronoUnit.DAYS.between(due, returned));
    }
}

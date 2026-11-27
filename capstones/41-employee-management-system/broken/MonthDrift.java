import java.time.LocalDate;

/** BROKEN: adding one month at a time is not the same as adding the months together. */
public class MonthDrift {
    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2025, 1, 31);
        LocalDate stepwise = start.plusMonths(1).plusMonths(1);
        LocalDate together = start.plusMonths(2);
        System.out.println("31 Jan, one month, one month: " + stepwise);
        System.out.println("31 Jan, two months at once:   " + together);
    }
}

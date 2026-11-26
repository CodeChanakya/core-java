import java.time.LocalDate;

/** Date-time classes are created with factory methods, not with new. */
public class NewLocalDate {

    public static void main(String[] args) {
        LocalDate date = new LocalDate(2026, 3, 5);
        System.out.println(date);
    }
}

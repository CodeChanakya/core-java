import java.time.LocalDate;

/** Date-time objects never change; every "change" returns a new object. */
public class ImmutableDate {

    public static void main(String[] args) {
        LocalDate dueDate = LocalDate.of(2026, 3, 6);
        dueDate.plusDays(7);
        System.out.println("Due in a week: " + dueDate);

        LocalDate extended = dueDate.plusDays(7);
        System.out.println("Extended     : " + extended);
    }
}

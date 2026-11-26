import java.time.ZoneId;
import java.time.ZonedDateTime;

/** The same moment, written on two different clocks. */
public class EqualsVsIsEqual {

    public static void main(String[] args) {
        ZonedDateTime newYork = ZonedDateTime.of(2026, 3, 9, 10, 0, 0, 0, ZoneId.of("America/New_York"));
        ZonedDateTime london = newYork.withZoneSameInstant(ZoneId.of("Europe/London"));

        System.out.println("New York: " + newYork);
        System.out.println("London  : " + london);
        System.out.println("equals()  : " + newYork.equals(london));
        System.out.println("isEqual() : " + newYork.isEqual(london));
        System.out.println("Local times say New York is earlier: "
                + newYork.toLocalDateTime().isBefore(london.toLocalDateTime()));
    }
}

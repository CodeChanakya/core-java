import java.util.List;

/** List.of(...) builds a list that can never change. */
public class ImmutableListAdd {

    public static void main(String[] args) {
        List<String> weekdays = List.of("Mon", "Tue", "Wed");
        System.out.println("Before: " + weekdays);
        weekdays.add("Thu");
        System.out.println("After: " + weekdays);
    }
}

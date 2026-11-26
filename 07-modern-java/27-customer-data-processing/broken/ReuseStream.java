import java.util.List;
import java.util.stream.Stream;

/** A stream can be consumed once. */
public class ReuseStream {

    public static void main(String[] args) {
        Stream<String> names = List.of("Asha", "Rahul", "Meera").stream();
        System.out.println("How many: " + names.count());
        System.out.println("First: " + names.findFirst().orElse("none"));
    }
}

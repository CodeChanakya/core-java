import java.util.List;
import java.util.stream.Stream;

/** BROKEN: a stream can be used only once. */
public class StreamsReuse {
    public static void main(String[] args) {
        Stream<String> paid = List.of("order 1", "order 2", "order 3").stream();
        System.out.println("How many: " + paid.count());
        try {
            System.out.println("First: " + paid.findFirst().orElse("none"));
        } catch (IllegalStateException e) {
            System.out.println("Second use failed: " + e.getMessage());
        }
    }
}

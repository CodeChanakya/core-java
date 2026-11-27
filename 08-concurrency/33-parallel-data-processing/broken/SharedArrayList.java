import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * ArrayList is not thread-safe. A parallel stream that adds to one from many threads loses
 * elements, and sometimes fails outright. The exact result changes from run to run.
 */
public class SharedArrayList {

    public static void main(String[] args) {
        for (int attempt = 1; attempt <= 20; attempt++) {
            List<Integer> shared = new ArrayList<>();
            try {
                IntStream.range(0, 100_000).parallel().forEach(shared::add);   // BUG: many threads, one ArrayList
            } catch (RuntimeException e) {
                System.out.println("Attempt " + attempt + ": it failed with " + e.getClass().getSimpleName());
                return;
            }
            if (shared.size() != 100_000) {
                System.out.println("Attempt " + attempt + ": expected 100000 elements but found " + shared.size());
                System.out.println("(your numbers will differ)");
                return;
            }
        }
        System.out.println("It worked 20 times by luck. That does not make it correct.");
    }
}

import java.util.ArrayList;
import java.util.List;

/** BROKEN: removing from a list while a for-each loop is walking through it. */
public class RemoveWhileLooping {
    public static void main(String[] args) {
        List<String> reservations = new ArrayList<>(List.of("Asha", "Bilal", "Chitra"));
        try {
            for (String name : reservations) {
                if (name.equals("Asha")) {
                    reservations.remove(name);     // Asha has borrowed the book: take her off the queue
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }
        System.out.println("The queue is now: " + reservations);
        List<String> again = new ArrayList<>(List.of("Asha", "Bilal", "Chitra"));
        again.removeIf(name -> name.equals("Asha"));
        System.out.println("With removeIf:    " + again);
    }
}

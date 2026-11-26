import java.util.ArrayList;
import java.util.List;

/** Removing from a list while a for-each loop is walking it. */
public class ConcurrentRemove {

    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Asha", "Rahul", "Meera", "Kiran"));
        for (String name : names) {
            if (name.startsWith("A")) {
                names.remove(name);
            }
        }
        System.out.println("Left: " + names);
    }
}

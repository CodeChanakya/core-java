import java.util.ArrayList;
import java.util.List;

/** A List<String> only accepts Strings, and the compiler enforces it. */
public class WrongType {

    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Asha");
        names.add(42);
        System.out.println(names);
    }
}

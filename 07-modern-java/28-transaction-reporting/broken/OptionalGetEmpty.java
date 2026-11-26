import java.util.List;
import java.util.Optional;

/** get() on an empty Optional is the same mistake as using a null. */
public class OptionalGetEmpty {

    public static void main(String[] args) {
        List<String> names = List.of("Asha", "Rahul");
        Optional<String> match = names.stream().filter(name -> name.startsWith("Z")).findFirst();
        System.out.println("Is there a match? " + match.isPresent());
        System.out.println("Name: " + match.get());
    }
}

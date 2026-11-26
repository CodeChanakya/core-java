import java.util.List;

/** Intermediate steps do nothing until a terminal step asks for a result. */
public class NoTerminalOperation {

    public static void main(String[] args) {
        List<String> names = List.of("Asha", "Rahul", "Meera");
        names.stream()
                .filter(name -> name.length() > 4)
                .peek(name -> System.out.println("Passed the filter: " + name));
        System.out.println("The pipeline above printed nothing.");

        long count = names.stream()
                .filter(name -> name.length() > 4)
                .peek(name -> System.out.println("Passed the filter: " + name))
                .count();
        System.out.println("This one ran, because count() is terminal: " + count);
    }
}

import java.util.List;

/** A lambda may read a local variable, but not change it. */
public class ModifyCapturedVariable {

    public static void main(String[] args) {
        List<String> names = List.of("Asha", "Rahul", "Meera");
        int count = 0;
        names.forEach(name -> count++);
        System.out.println("Names: " + count);
    }
}

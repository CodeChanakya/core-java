import java.util.ArrayList;
import java.util.List;

/** A raw list (no type in angle brackets) switches off the compiler's checking and moves the failure to run time. */
public class RawTypeCrash {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        List names = new ArrayList();
        names.add("Asha");
        names.add(42);

        for (Object item : names) {
            String name = (String) item;
            System.out.println(name.toUpperCase());
        }
    }
}

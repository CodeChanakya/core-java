import java.util.HashSet;
import java.util.Set;

/** A Set has no positions, so there is no get(index). */
public class SetIndexAccess {

    public static void main(String[] args) {
        Set<String> cities = new HashSet<>();
        cities.add("Kochi");
        cities.add("Pune");
        String first = cities.get(0);
        System.out.println(first);
    }
}

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/** A TreeMap has to compare keys, and nothing can be compared with null. */
public class TreeMapNullKey {

    public static void main(String[] args) {
        Map<String, Integer> hash = new HashMap<>();
        hash.put(null, 1);
        System.out.println("HashMap accepts a null key: " + hash);

        Map<String, Integer> tree = new TreeMap<>();
        tree.put(null, 1);
        System.out.println("TreeMap accepts a null key: " + tree);
    }
}

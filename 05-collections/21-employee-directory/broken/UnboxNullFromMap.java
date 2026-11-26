import java.util.HashMap;
import java.util.Map;

/** map.get() returns null for a missing key, and null cannot become an int. */
public class UnboxNullFromMap {

    public static void main(String[] args) {
        Map<String, Integer> stock = new HashMap<>();
        stock.put("pen", 20);
        System.out.println("Pens: " + stock.get("pen"));
        int notebooks = stock.get("notebook");
        System.out.println("Notebooks: " + notebooks);
    }
}

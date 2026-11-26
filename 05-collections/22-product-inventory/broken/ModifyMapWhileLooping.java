import java.util.LinkedHashMap;
import java.util.Map;

/** Removing entries from a map while a for-each walks it. */
public class ModifyMapWhileLooping {

    public static void main(String[] args) {
        Map<String, Integer> stock = new LinkedHashMap<>();
        stock.put("pen", 0);
        stock.put("notebook", 12);
        stock.put("cable", 0);
        stock.put("lamp", 3);

        for (String sku : stock.keySet()) {
            if (stock.get(sku) == 0) {
                stock.remove(sku);
            }
        }
        System.out.println("Left: " + stock);
    }
}

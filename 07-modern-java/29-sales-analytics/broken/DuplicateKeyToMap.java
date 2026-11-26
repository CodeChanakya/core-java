import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** toMap does not know what to do when two items share a key, unless you tell it. */
public class DuplicateKeyToMap {

    record Sale(String salesperson, int units) {
    }

    public static void main(String[] args) {
        List<Sale> sales = List.of(new Sale("Asha", 3), new Sale("Rahul", 2), new Sale("Asha", 5));
        Map<String, Integer> units = sales.stream()
                .collect(Collectors.toMap(Sale::salesperson, Sale::units));
        System.out.println(units);
    }
}

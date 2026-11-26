import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** groupingBy cannot put an item in a bucket named null. */
public class NullGroupingKey {

    record Customer(String name, String city) {
    }

    public static void main(String[] args) {
        List<Customer> customers = List.of(new Customer("Asha", "Kochi"), new Customer("Rahul", null));
        Map<String, Long> perCity = customers.stream()
                .collect(Collectors.groupingBy(Customer::city, Collectors.counting()));
        System.out.println(perCity);
    }
}

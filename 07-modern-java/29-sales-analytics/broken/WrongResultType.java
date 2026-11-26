import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** counting() produces a Long, not an Integer. */
public class WrongResultType {

    public static void main(String[] args) {
        List<String> cities = List.of("Kochi", "Pune", "Kochi");
        Map<String, Integer> perCity = cities.stream()
                .collect(Collectors.groupingBy(city -> city, Collectors.counting()));
        System.out.println(perCity);
    }
}

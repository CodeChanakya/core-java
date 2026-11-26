import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** The map promises a List of products per category, not a single product. */
public class WrongValueType {

    enum Category { HOME, STATIONERY }

    static class Product {
        String name;
        Product(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Map<Category, List<Product>> groups = new EnumMap<>(Category.class);
        groups.put(Category.HOME, new Product("Desk Lamp"));
        System.out.println(groups);
    }
}

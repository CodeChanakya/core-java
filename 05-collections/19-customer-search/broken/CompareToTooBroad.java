import java.util.Set;
import java.util.TreeSet;

/** compareTo decides what counts as "the same" in a TreeSet. */
public class CompareToTooBroad {

    static class Customer implements Comparable<Customer> {
        String name;
        String email;

        Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }

        @Override
        public int compareTo(Customer other) {
            return name.compareTo(other.name);        // ignores the email completely
        }
    }

    public static void main(String[] args) {
        Set<Customer> customers = new TreeSet<>();
        customers.add(new Customer("Asha Menon", "asha@example.com"));
        customers.add(new Customer("Asha Menon", "asha.m@example.com"));
        System.out.println("Two different people added, set holds: " + customers.size());
    }
}

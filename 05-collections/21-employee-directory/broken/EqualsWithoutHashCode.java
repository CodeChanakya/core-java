import java.util.HashSet;
import java.util.Set;

/** equals() was overridden but hashCode() was not. */
public class EqualsWithoutHashCode {

    static class Employee {
        String id;

        Employee(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Employee && ((Employee) other).id.equals(id);
        }
    }

    public static void main(String[] args) {
        Employee a = new Employee("E-104");
        Employee b = new Employee("E-104");
        System.out.println("a.equals(b): " + a.equals(b));

        Set<Employee> team = new HashSet<>();
        team.add(a);
        team.add(b);
        System.out.println("Set size: " + team.size());
        System.out.println("Contains a copy of a: " + team.contains(new Employee("E-104")));
    }
}

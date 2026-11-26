import java.util.Set;
import java.util.TreeSet;

/** A TreeSet must be able to put its elements in order. */
public class TreeSetNotComparable {

    static class Person {
        String name;
        Person(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Set<Person> people = new TreeSet<>();
        System.out.println("Adding the first person...");
        people.add(new Person("Asha"));
        System.out.println("Done");
    }
}

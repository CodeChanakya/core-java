/** The parent has no empty constructor, so every child must say how to build the parent part. */
public class MissingSuperCall {

    public static void main(String[] args) {
        Developer dev = new Developer("Asha", "Java");
        System.out.println(dev.name + " writes " + dev.language);
    }
}

class Employee {
    String name;

    Employee(String name) {
        this.name = name;
    }
}

class Developer extends Employee {
    String language;

    Developer(String name, String language) {
        this.language = language;
    }
}

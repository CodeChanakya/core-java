/** Employee is a record, so its fields are final. A change must go through withSalary(...) and be saved. */
public class RecordIsImmutable {

    record Employee(String name, int salary) {
    }

    public static void main(String[] args) {
        Employee asha = new Employee("Asha", 90000);
        asha.salary = 95000;
        System.out.println(asha);
    }
}

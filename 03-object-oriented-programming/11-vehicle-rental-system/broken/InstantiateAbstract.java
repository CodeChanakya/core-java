/** An abstract class is a template. You can extend it, but you cannot create one directly. */
public class InstantiateAbstract {

    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle();
        System.out.println(vehicle.dailyRate());
    }
}

abstract class Vehicle {
    abstract double dailyRate();
}

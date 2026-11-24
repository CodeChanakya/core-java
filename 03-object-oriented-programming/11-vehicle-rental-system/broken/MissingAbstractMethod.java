/** A concrete subclass must fill in every abstract method of its parent. */
public class MissingAbstractMethod {

    public static void main(String[] args) {
        Vehicle truck = new Truck();
        System.out.println(truck.dailyRate());
    }
}

abstract class Vehicle {
    abstract double dailyRate();

    abstract String kind();
}

class Truck extends Vehicle {
    @Override
    double dailyRate() {
        return 4500;
    }
}

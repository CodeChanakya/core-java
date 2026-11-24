/** Interface methods are public, so the class that implements them must be public too. */
public class WeakerAccess {

    public static void main(String[] args) {
        Insurable car = new Car();
        System.out.println(car.insurancePerDay());
    }
}

interface Insurable {
    double insurancePerDay();
}

class Car implements Insurable {
    double insurancePerDay() {
        return 300;
    }
}

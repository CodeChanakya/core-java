/** Only the classes named in permits may implement a sealed interface. */
public class NotPermitted {

    sealed interface Shape permits Circle {
    }

    record Circle(double radius) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    public static void main(String[] args) {
        System.out.println(new Square(2));
    }
}

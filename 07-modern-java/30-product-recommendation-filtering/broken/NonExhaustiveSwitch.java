/** A switch over a sealed type must cover every permitted case. */
public class NonExhaustiveSwitch {

    sealed interface Shape permits Circle, Square, Triangle {
    }

    record Circle(double radius) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    record Triangle(double base, double height) implements Shape {
    }

    static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Square s -> s.side() * s.side();
        };
    }

    public static void main(String[] args) {
        System.out.println(area(new Circle(1)));
    }
}

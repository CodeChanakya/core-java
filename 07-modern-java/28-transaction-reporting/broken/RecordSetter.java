/** Record fields are final: there is no way to change one after construction. */
public class RecordSetter {

    record Point(int x, int y) {
    }

    public static void main(String[] args) {
        Point point = new Point(2, 3);
        point.x = 10;
        System.out.println(point);
    }
}

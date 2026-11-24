import java.util.ArrayList;
import java.util.List;

/** Generics work with objects. Primitives need their wrapper classes: int becomes Integer. */
public class PrimitiveTypeArgument {

    public static void main(String[] args) {
        List<int> marks = new ArrayList<>();
        marks.add(90);
        System.out.println(marks);
    }
}

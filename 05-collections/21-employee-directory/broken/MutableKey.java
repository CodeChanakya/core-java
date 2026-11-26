import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Changing a key after it went into a HashMap loses it. */
public class MutableKey {

    static class Badge {
        String code;

        Badge(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Badge && ((Badge) other).code.equals(code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code);
        }
    }

    public static void main(String[] args) {
        Map<Badge, String> owners = new HashMap<>();
        Badge badge = new Badge("B-1");
        owners.put(badge, "Asha");
        System.out.println("Before: " + owners.get(badge));

        badge.code = "B-2";                     // the key changed after being stored
        System.out.println("After : " + owners.get(badge));
        System.out.println("Size  : " + owners.size());
    }
}

// This file does not compile, on purpose. See README.md in this folder.
public class MissingReturn {

    static String slabFor(int units) {
        if (units <= 100) {
            return "slab 1";
        } else if (units <= 200) {
            return "slab 2";
        }
    }

    public static void main(String[] args) {
        System.out.println(slabFor(150));
    }
}

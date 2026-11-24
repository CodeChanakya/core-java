// This file compiles and runs, but gives a surprising answer. See README.md in this folder.
public class StringEquality {

    public static void main(String[] args) {
        String stored = "SAVE10";
        String typed = new String("SAVE10");
        System.out.println("== says     : " + (stored == typed));
        System.out.println("equals says: " + stored.equals(typed));
    }
}

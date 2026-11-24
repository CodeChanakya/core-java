// This file compiles but crashes when run, on purpose. See README.md in this folder.
public class OffByOne {

    public static void main(String[] args) {
        int[] marks = {92, 88, 95, 90};
        int total = 0;
        for (int i = 0; i <= marks.length; i++) {
            total += marks[i];
        }
        System.out.println("Total: " + total);
    }
}

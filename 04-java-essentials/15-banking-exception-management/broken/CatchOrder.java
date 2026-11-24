/** Catch blocks are tried top to bottom. A general one first makes the specific one unreachable. */
public class CatchOrder {

    public static void main(String[] args) {
        try {
            int[] marks = new int[3];
            marks[5] = 90;
        } catch (Exception e) {
            System.out.println("Something went wrong");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Bad index");
        }
    }
}

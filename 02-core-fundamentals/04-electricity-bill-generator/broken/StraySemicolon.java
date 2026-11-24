// This file compiles and runs, but prints the wrong thing. See README.md in this folder.
public class StraySemicolon {

    public static void main(String[] args) {
        int units = 50;
        if (units > 200);
        {
            System.out.println("High usage warning");
        }
        System.out.println("Units: " + units);
    }
}

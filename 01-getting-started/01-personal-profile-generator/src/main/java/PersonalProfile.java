/**
 * Prints a short personal profile to the console.
 *
 * This is the first program in the course, so it stays small on purpose:
 * store a few facts, then print them as a labelled card.
 */
public class PersonalProfile {

    public static void main(String[] args) {
        // Variables get a full lesson in Phase 2. For now, read each line as
        // "give this value a name". The word in front says what kind of value
        // it is: String is text, int is a whole number.
        String name = "Priya Nair";
        int age = 29;
        String city = "Pune";
        String role = "QA engineer";
        String learning = "Java";

        System.out.println("==============================");
        System.out.println("  PERSONAL PROFILE");
        System.out.println("==============================");
        System.out.println("Name     : " + name);
        System.out.println("Age      : " + age);
        System.out.println("City     : " + city);
        System.out.println("Role     : " + role);
        System.out.println("Learning : " + learning);
        System.out.println("==============================");
    }
}

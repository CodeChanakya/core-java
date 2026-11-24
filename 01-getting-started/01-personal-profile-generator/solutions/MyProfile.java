/**
 * One possible answer to all six exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MyProfile {

    public static void main(String[] args) {
        String name = "Priya Nair";
        int age = 29;
        String city = "Pune";
        String country = "India";
        String role = "QA engineer";
        String learning = "Java";
        String hobby = "Trail running";
        String favouriteLanguage = "Java";

        System.out.println("==============================");
        System.out.println("  PERSONAL PROFILE");
        System.out.println("==============================");
        System.out.println();

        // %-20s pads the label with spaces up to 20 characters, so every
        // colon lands in the same column. %n is the portable line break.
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Age", age);
        System.out.printf("%-20s: %s%n", "Age in months", age * 12);
        System.out.printf("%-20s: %s%n", "City", city + ", " + country);
        System.out.printf("%-20s: %s%n", "Role", role);
        System.out.printf("%-20s: %s%n", "Learning", learning);
        System.out.printf("%-20s: %s%n", "Hobby", hobby);
        System.out.printf("%-20s: %s%n", "Favourite language", favouriteLanguage);

        System.out.println("==============================");
    }
}

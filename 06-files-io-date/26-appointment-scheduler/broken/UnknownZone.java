import java.time.ZoneId;

/** Zone ids are region names, not abbreviations or guesses. */
public class UnknownZone {

    public static void main(String[] args) {
        System.out.println("Good: " + ZoneId.of("Asia/Kolkata"));
        System.out.println("Bad : " + ZoneId.of("India/Mumbai"));
    }
}

import java.util.Locale;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MyMarks {

    public static void main(String[] args) {
        String[] students = {"Asha", "Rahul", "Meera", "Kiran", "Dev"};     // B1: a fifth student
        int[][] marks = {
            {92, 88, 95, 90},
            {67, 54, 71, 60},
            {81, 79, 85, 77},
            {45, 38, 52, 41},
            {72, 35, 68, 74}
        };

        // C1: sort row numbers by average, best first. We swap row numbers,
        // not the marks, so the names stay matched to their rows.
        int[] order = new int[students.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        for (int pass = 0; pass < order.length - 1; pass++) {
            for (int i = 0; i < order.length - 1 - pass; i++) {
                if (average(marks[order[i]]) < average(marks[order[i + 1]])) {
                    int temp = order[i];
                    order[i] = order[i + 1];
                    order[i + 1] = temp;
                }
            }
        }

        System.out.println("Rank  Name    Average  Lowest  Result");
        int failed = 0;
        for (int rank = 0; rank < order.length; rank++) {
            int row = order[rank];
            int lowest = lowest(marks[row]);
            // I1: a student passes only if every subject is at least 40.
            String result = lowest >= 40 ? "Pass" : "Fail";
            if (lowest < 40) {
                failed++;                                                             // I2
            }
            System.out.printf(Locale.US, "%4d  %-6s %8.2f %7d  %s%n",
                    rank + 1, students[row], average(marks[row]), lowest, result);   // B2
        }
        System.out.println("Topper: " + students[order[0]]);                          // B3
        System.out.println("Failed: " + failed + " of " + students.length);           // I2
    }

    static double average(int[] marks) {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return (double) sum / marks.length;
    }

    static int lowest(int[] marks) {
        int worst = marks[0];
        for (int mark : marks) {
            if (mark < worst) {
                worst = mark;
            }
        }
        return worst;
    }
}

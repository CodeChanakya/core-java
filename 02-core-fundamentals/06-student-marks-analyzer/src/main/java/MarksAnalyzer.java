import java.util.Locale;

/**
 * Analyses the marks of a small class: a report card per student, then
 * figures for the whole class.
 *
 * The marks live in a two-dimensional array: one row per student, one column
 * per subject. Both arrays are matched by position, so students[1] and
 * marks[1] describe the same person. Phase 3 replaces this with objects.
 */
public class MarksAnalyzer {

    private static final String[] SUBJECTS = {"Maths", "Science", "English", "History"};

    public static void main(String[] args) {
        String[] students = {"Asha", "Rahul", "Meera", "Kiran"};
        int[][] marks = {
            {92, 88, 95, 90},
            {67, 54, 71, 60},
            {81, 79, 85, 77},
            {45, 38, 52, 41}
        };

        printHeader();
        for (int i = 0; i < students.length; i++) {
            printReportRow(students[i], marks[i]);
        }

        int topper = topperIndex(marks);
        System.out.println();
        System.out.printf(Locale.US, "Class average : %.2f%n", average(marks));
        System.out.println("Highest mark  : " + highest(marks));
        System.out.printf(Locale.US, "Topper        : %s (average %.2f)%n", students[topper], average(marks[topper]));
        System.out.println();
        System.out.println("Average per subject");
        for (int subject = 0; subject < SUBJECTS.length; subject++) {
            System.out.printf(Locale.US, "  %-8s %.2f%n", SUBJECTS[subject], subjectAverage(marks, subject));
        }
    }

    private static void printHeader() {
        System.out.printf("%-8s", "Name");
        for (String subject : SUBJECTS) {
            System.out.printf("%9s", subject);
        }
        System.out.printf("%7s%9s%7s%n", "Total", "Average", "Grade");
    }

    private static void printReportRow(String name, int[] marks) {
        System.out.printf("%-8s", name);
        for (int mark : marks) {
            System.out.printf("%9d", mark);
        }
        double average = average(marks);
        System.out.printf(Locale.US, "%7d%9.2f%7s%n", total(marks), average, gradeFor(average));
    }

    static int total(int[] marks) {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return sum;
    }

    // Two methods with the same name but different parameter types: Java picks
    // the right one by looking at the argument you pass. This is overloading.
    static double average(int[] marks) {
        return (double) total(marks) / marks.length;
    }

    static double average(int[][] table) {
        int sum = 0;
        int count = 0;
        for (int[] row : table) {
            sum += total(row);
            count += row.length;
        }
        return (double) sum / count;
    }

    static int highest(int[] marks) {
        int best = marks[0];
        for (int mark : marks) {
            if (mark > best) {
                best = mark;
            }
        }
        return best;
    }

    // Overloaded again: the whole table, found by asking each row for its best.
    static int highest(int[][] table) {
        int best = table[0][0];
        for (int[] row : table) {
            best = Math.max(best, highest(row));
        }
        return best;
    }

    static double subjectAverage(int[][] table, int subject) {
        int sum = 0;
        for (int[] row : table) {
            sum += row[subject];
        }
        return (double) sum / table.length;
    }

    /** Returns the row number of the student with the best average. */
    static int topperIndex(int[][] table) {
        int best = 0;
        for (int i = 1; i < table.length; i++) {
            if (average(table[i]) > average(table[best])) {
                best = i;
            }
        }
        return best;
    }

    static String gradeFor(double average) {
        if (average >= 90) {
            return "A";
        } else if (average >= 75) {
            return "B";
        } else if (average >= 60) {
            return "C";
        } else if (average >= 40) {
            return "D";
        } else {
            return "F";
        }
    }
}

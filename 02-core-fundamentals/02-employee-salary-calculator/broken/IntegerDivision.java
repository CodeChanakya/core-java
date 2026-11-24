// This file compiles and runs, but prints the wrong answer. See README.md in this folder.
public class IntegerDivision {

    public static void main(String[] args) {
        int daysWorked = 26;
        int workingDays = 30;
        int percent = daysWorked / workingDays * 100;
        System.out.println("Attendance: " + percent + "%");
    }
}

import java.util.Locale;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MySalary {

    public static void main(String[] args) {
        int basicSalary = 62000;                           // B1: your own number
        int specialAllowance = 5000;
        int performanceBonus = 3000;                       // B2
        double hra = basicSalary * 0.40;
        double grossSalary = basicSalary + hra + specialAllowance + performanceBonus;

        double providentFund = basicSalary * 0.12;
        int professionalTax = 200;
        double incomeTax = grossSalary * 0.05;             // I1: flat 5% of gross
        double netSalary = grossSalary - providentFund - professionalTax - incomeTax;

        int workingDays = 30;
        int daysWorked = 26;
        double perDay = netSalary / workingDays;           // B3
        double attendance = daysWorked * 100.0 / workingDays;   // I2: 100.0 makes it a double

        // C1: an 8% raise, rounded to the nearest whole rupee and kept as an int.
        int revisedBasic = (int) Math.round(basicSalary * 1.08);
        double annualBefore = (basicSalary + basicSalary * 0.40 + specialAllowance + performanceBonus) * 12;
        double annualAfter = (revisedBasic + revisedBasic * 0.40 + specialAllowance + performanceBonus) * 12;

        System.out.printf(Locale.US, "Gross salary     : %.2f%n", grossSalary);
        System.out.printf(Locale.US, "Income tax (5%%)  : %.2f%n", incomeTax);
        System.out.printf(Locale.US, "Net salary       : %.2f%n", netSalary);
        System.out.printf(Locale.US, "Net per day      : %.2f%n", perDay);
        System.out.printf(Locale.US, "Attendance       : %.2f%%%n", attendance);
        System.out.println("Revised basic    : " + revisedBasic);
        System.out.printf(Locale.US, "Annual gross     : %.2f -> %.2f%n", annualBefore, annualAfter);
    }
}

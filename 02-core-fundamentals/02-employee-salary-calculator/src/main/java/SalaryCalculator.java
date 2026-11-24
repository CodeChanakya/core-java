import java.util.Locale;

/**
 * Works out one employee's monthly salary slip from their basic salary.
 *
 * Everything happens inside main so you can follow the numbers from top to
 * bottom. Methods arrive later in this phase.
 */
public class SalaryCalculator {

    public static void main(String[] args) {
        // Money is kept in double here to keep the arithmetic easy to read.
        // Program 3 shows why real payroll code should not do this.
        int basicSalary = 50000;
        int specialAllowance = 5000;
        double hra = basicSalary * 0.40;            // house rent allowance, 40% of basic
        double grossSalary = basicSalary + hra + specialAllowance;

        double providentFund = basicSalary * 0.12;  // 12% of basic
        int professionalTax = 200;
        double totalDeductions = providentFund + professionalTax;

        double netSalary = grossSalary - totalDeductions;

        // This employee worked 26 of the 30 working days.
        int daysWorked = 26;
        int workingDays = 30;

        // Both lines look reasonable. Only one of them is right.
        int wrongPercent = daysWorked / workingDays * 100;
        int rightPercent = daysWorked * 100 / workingDays;

        double payable = netSalary * daysWorked / workingDays;
        int payableCutOff = (int) payable;           // the cast chops off the decimals
        long payableRounded = Math.round(payable);   // rounds to the nearest whole number

        System.out.println("===== SALARY SLIP =====");
        System.out.printf(Locale.US, "Basic salary      : %d%n", basicSalary);
        System.out.printf(Locale.US, "HRA (40%%)         : %.2f%n", hra);
        System.out.printf(Locale.US, "Special allowance : %d%n", specialAllowance);
        System.out.printf(Locale.US, "Gross salary      : %.2f%n", grossSalary);
        System.out.printf(Locale.US, "Provident fund    : %.2f%n", providentFund);
        System.out.printf(Locale.US, "Professional tax  : %d%n", professionalTax);
        System.out.printf(Locale.US, "Net salary        : %.2f%n", netSalary);
        System.out.println();
        System.out.println("===== ATTENDANCE =====");
        System.out.println("Days worked       : " + daysWorked + " of " + workingDays);
        System.out.println("Wrong percentage  : " + wrongPercent);
        System.out.println("Right percentage  : " + rightPercent);
        System.out.printf(Locale.US, "Payable amount    : %.2f%n", payable);
        System.out.println("Cut off to whole  : " + payableCutOff);
        System.out.println("Rounded to whole  : " + payableRounded);
        System.out.println();
        System.out.printf(Locale.US, "Net salary, 12 months: %.2f%n", netSalary * 12);
    }
}

/** @Override asks the compiler to check that a parent method really exists. */
public class OverrideTypo {

    public static void main(String[] args) {
        Employee boss = new Manager();
        System.out.println("Pay: " + boss.monthlyPay());
    }
}

class Employee {
    double monthlyPay() {
        return 50000;
    }
}

class Manager extends Employee {
    @Override
    double monthlypay() {
        return 80000;
    }
}

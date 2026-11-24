/**
 * Prints a small bill table for a few sample readings.
 * Read BillCalculator.java first, then this file.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Units  Slab    Bill");
        printBill(0);
        printBill(50);
        printBill(100);
        printBill(101);
        printBill(150);
        printBill(250);
    }

    // The edge values 100 and 101 are included on purpose: bugs hide at the
    // boundary between two slabs.
    private static void printBill(int units) {
        System.out.println(units + "  " + BillCalculator.slabFor(units) + "  " + BillCalculator.billFor(units));
    }
}

/** The classic CSV mistake: split(",") silently throws away empty cells at the END of the line. */
public class SplitDropsEmptyCells {

    public static void main(String[] args) {
        String line = "P-400,Eraser,,";   // the price and the tax rate are missing

        System.out.println("split(\",\")     -> " + line.split(",").length + " cells");
        System.out.println("split(\",\", -1) -> " + line.split(",", -1).length + " cells");
    }
}

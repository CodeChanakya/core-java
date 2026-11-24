// This file compiles and runs, but changes nothing. See README.md in this folder.
public class IgnoredResult {

    public static void main(String[] args) {
        String coupon = "  save10 ";
        coupon.trim();
        coupon.toUpperCase();
        System.out.println("[" + coupon + "]");
    }
}

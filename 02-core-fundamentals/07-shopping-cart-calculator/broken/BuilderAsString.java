// This file does not compile, on purpose. See README.md in this folder.
public class BuilderAsString {

    public static void main(String[] args) {
        StringBuilder text = new StringBuilder("Receipt");
        String result = text;
        System.out.println(result);
    }
}

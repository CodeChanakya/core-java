/** Reflection finds classes by text, so a typo is not a compile error. It is an exception at run time. */
public class ClassNameTypo {

    public static void main(String[] args) throws Exception {
        Class<?> type = Class.forName("com.example.plugins.ReportPlgin");
        System.out.println("Found " + type.getSimpleName());
    }
}

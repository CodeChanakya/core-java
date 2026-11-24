/** An annotation attribute with no default must be given a value every time. */
public class MissingAnnotationValue {

    @interface PluginInfo {
        String name();
        int order() default 100;
    }

    @PluginInfo(order = 5)
    static class Report {
    }

    public static void main(String[] args) {
        System.out.println("Loaded");
    }
}

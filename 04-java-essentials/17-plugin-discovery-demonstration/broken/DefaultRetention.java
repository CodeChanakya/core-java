import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Without RUNTIME retention the annotation is thrown away before the program runs, so reflection cannot see it. */
public class DefaultRetention {

    @interface Forgotten {
        String name();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Kept {
        String name();
    }

    @Forgotten(name = "demo")
    static class First {
    }

    @Kept(name = "demo")
    static class Second {
    }

    public static void main(String[] args) {
        System.out.println("First  sees: " + First.class.getAnnotation(Forgotten.class));
        System.out.println("Second sees: " + Second.class.getAnnotation(Kept.class));
    }
}

import org.junit.Test;

/** A test written the JUnit 4 way. JUnit 5 lives in a different package. */
public class JUnit4Import {

    @Test
    public void addsUp() {
        if (1 + 1 != 2) {
            throw new AssertionError();
        }
    }
}

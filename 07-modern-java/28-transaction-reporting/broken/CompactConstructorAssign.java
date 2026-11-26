/** In a compact constructor the fields are assigned for you, at the end. */
public class CompactConstructorAssign {

    record Price(int amount) {
        Price {
            if (amount < 0) {
                throw new IllegalArgumentException("negative");
            }
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Price(5));
    }
}

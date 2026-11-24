import java.math.BigDecimal;

/** The constructor parameter has the same name as the field. */
public class ForgottenThis {

    public static void main(String[] args) {
        Account account = new Account("Asha", new BigDecimal("500.00"));
        System.out.println("Holder : " + account.getHolder());
        System.out.println("Balance: " + account.getBalance());
    }
}

class Account {
    private String holder;
    private BigDecimal balance;

    Account(String holder, BigDecimal balance) {
        holder = holder;
        balance = balance;
    }

    String getHolder() {
        return holder;
    }

    BigDecimal getBalance() {
        return balance;
    }
}

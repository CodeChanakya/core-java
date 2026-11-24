import java.math.BigDecimal;

/** The variable was declared, but no object was ever created for it. */
public class NullReference {

    public static void main(String[] args) {
        Account account;
        account = findAccount("ACC-9999");
        account.deposit(new BigDecimal("50.00"));
        System.out.println("Deposit done");
    }

    // A lookup that finds nothing returns null, just like Bank.find.
    static Account findAccount(String number) {
        return null;
    }
}

class Account {
    private BigDecimal balance = BigDecimal.ZERO;

    void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}

import java.math.BigDecimal;

/** The balance is private, so the outside world cannot set it directly. */
public class PrivateAccess {

    public static void main(String[] args) {
        Account account = new Account(new BigDecimal("100.00"));
        account.balance = new BigDecimal("1000000.00");
        System.out.println("Balance: " + account.balance);
    }
}

class Account {
    private BigDecimal balance;

    Account(BigDecimal openingBalance) {
        this.balance = openingBalance;
    }
}

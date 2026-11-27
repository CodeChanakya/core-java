package com.codechanakya.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * The bank's business rules. It knows nothing about SQL and gets today's date from an injected clock.
 *
 * <p>Rules: amounts must be positive; a customer may withdraw at most 50,000 per account per day;
 * a transfer moves money between two different accounts as one unit; savings accounts earn interest
 * of 4 percent a year, paid monthly.
 */
public class BankService {

    static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("50000.00");
    static final BigDecimal MONTHLY_FEE = new BigDecimal("100.00");
    static final BigDecimal FEE_THRESHOLD = new BigDecimal("5000.00");
    static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.04");

    private final BankStore store;
    private final Clock clock;

    public BankService(BankStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    public Account open(String customer, AccountType type, BigDecimal opening) {
        if (customer == null || customer.isBlank()) {
            throw new BankException("A customer name is required.");
        }
        if (opening.compareTo(type.minimumBalance()) < 0) {
            throw new BankException("A " + type + " account needs at least " + type.minimumBalance().toPlainString()
                    + " to open, but " + opening.setScale(2, RoundingMode.HALF_UP).toPlainString() + " was given");
        }
        return store.open(customer.trim(), type, opening, today());
    }

    public void deposit(int accountId, BigDecimal amount) {
        requirePositive(amount);
        store.post(today(), List.of(new Entry(accountId, TransactionKind.DEPOSIT, amount, "Deposit")));
    }

    public void withdraw(int accountId, BigDecimal amount) {
        requirePositive(amount);
        requireAccount(accountId);
        BigDecimal soFar = store.withdrawnOn(accountId, today());
        if (soFar.add(amount).compareTo(DAILY_WITHDRAWAL_LIMIT) > 0) {
            throw new BankException("Daily withdrawal limit of " + DAILY_WITHDRAWAL_LIMIT.toPlainString()
                    + " exceeded (already withdrawn " + soFar.toPlainString() + ")");
        }
        store.post(today(), List.of(new Entry(accountId, TransactionKind.WITHDRAWAL, amount.negate(), "Withdrawal")));
    }

    public void transfer(int from, int to, BigDecimal amount) {
        requirePositive(amount);
        if (from == to) {
            throw new BankException("Cannot transfer to the same account");
        }
        store.post(today(), List.of(
                new Entry(from, TransactionKind.TRANSFER_OUT, amount.negate(), "Transfer to " + to),
                new Entry(to, TransactionKind.TRANSFER_IN, amount, "Transfer from " + from)));
    }

    /** Pays one month of interest to every savings account. Returns how many accounts were paid. */
    public int payMonthlyInterest() {
        int paid = 0;
        for (Account account : store.findAll()) {
            if (account.type() != AccountType.SAVINGS) {
                continue;
            }
            BigDecimal interest = account.balance().multiply(ANNUAL_INTEREST_RATE)
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            if (interest.signum() > 0) {
                store.post(today(), List.of(new Entry(account.id(), TransactionKind.INTEREST, interest, "Monthly interest")));
                paid++;
            }
        }
        return paid;
    }

    /**
     * Exercise 2: charges a flat monthly fee to every current account holding less than the threshold.
     * The fee may push an account into overdraft, but never past its limit: such an account is skipped.
     */
    public int chargeMonthlyFees() {
        int charged = 0;
        for (Account account : store.findAll()) {
            boolean small = account.balance().compareTo(FEE_THRESHOLD) < 0;
            boolean canAfford = account.balance().subtract(MONTHLY_FEE).compareTo(account.type().floor()) >= 0;
            if (account.type() == AccountType.CURRENT && small && canAfford) {
                store.post(today(), List.of(new Entry(account.id(), TransactionKind.FEE, MONTHLY_FEE.negate(), "Monthly fee")));
                charged++;
            }
        }
        return charged;
    }

    /** Exercise 3: the history of one account between two dates, both included. */
    public List<Transaction> history(int accountId, LocalDate from, LocalDate to) {
        return history(accountId).stream()
                .filter(t -> !t.date().isBefore(from) && !t.date().isAfter(to))
                .toList();
    }

    public Account account(int id) {
        return requireAccount(id);
    }

    public List<Account> accounts() {
        return store.findAll();
    }

    public List<Transaction> history(int accountId) {
        requireAccount(accountId);
        return store.history(accountId);
    }

    /** The total of every balance in the bank. */
    public BigDecimal totalDeposits() {
        return store.findAll().stream().map(Account::balance).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    private Account requireAccount(int id) {
        return store.find(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    private static void requirePositive(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new BankException("The amount must be positive, but was " + amount.toPlainString());
        }
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}

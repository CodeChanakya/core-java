package com.codechanakya.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** Keeps everything in maps and lists. Fast, and used by most tests. */
public class InMemoryBankStore implements BankStore {

    private final Map<Integer, Account> accounts = new TreeMap<>();
    private final List<Transaction> ledger = new ArrayList<>();
    private int nextAccountId = 1;
    private int nextTransactionId = 1;

    @Override
    public Account open(String customer, AccountType type, BigDecimal opening, LocalDate date) {
        Account account = new Account(nextAccountId++, customer, type, opening);
        if (account.balance().compareTo(type.floor()) < 0) {
            throw new InsufficientFundsException(account.id());
        }
        accounts.put(account.id(), account);
        record(account.id(), TransactionKind.OPENING, account.balance(), account.balance(), "Account opened", date);
        return account;
    }

    @Override
    public Optional<Account> find(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void post(LocalDate date, List<Entry> entries) {
        // Work on copies, and only publish them when every entry has been accepted.
        Map<Integer, Account> working = new HashMap<>();
        List<Transaction> pending = new ArrayList<>();
        int transactionId = nextTransactionId;
        for (Entry entry : entries) {
            Account current = working.getOrDefault(entry.accountId(), accounts.get(entry.accountId()));
            if (current == null) {
                throw new AccountNotFoundException(entry.accountId());
            }
            Account updated = current.withBalance(current.balance().add(entry.amount()));
            if (updated.balance().compareTo(current.type().floor()) < 0) {
                throw new InsufficientFundsException(current.id());
            }
            working.put(updated.id(), updated);
            pending.add(new Transaction(transactionId++, updated.id(), entry.kind(), entry.amount(),
                    updated.balance(), entry.note(), date));
        }
        accounts.putAll(working);
        ledger.addAll(pending);
        nextTransactionId = transactionId;
    }

    @Override
    public List<Transaction> history(int accountId) {
        return ledger.stream().filter(t -> t.accountId() == accountId).toList();
    }

    @Override
    public BigDecimal withdrawnOn(int accountId, LocalDate date) {
        return ledger.stream()
                .filter(t -> t.accountId() == accountId && t.date().equals(date) && t.kind() == TransactionKind.WITHDRAWAL)
                .map(t -> t.amount().negate())
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    private void record(int accountId, TransactionKind kind, BigDecimal amount, BigDecimal after, String note, LocalDate date) {
        ledger.add(new Transaction(nextTransactionId++, accountId, kind, amount, after, note, date));
    }
}

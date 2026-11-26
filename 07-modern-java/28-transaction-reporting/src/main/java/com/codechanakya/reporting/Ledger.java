package com.codechanakya.reporting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** Holds transactions and answers questions about them. Several answers may be "nothing", hence Optional. */
public class Ledger {

    private final List<Transaction> transactions = new ArrayList<>();
    private final List<String> rejected = new ArrayList<>();

    /** Loads CSV text. Lines that do not parse are remembered, not fatal. */
    public void load(String csv) {
        int lineNumber = 0;
        for (String line : csv.lines().toList()) {          // String.lines() splits on any line ending
            lineNumber++;
            if (line.isBlank() || line.startsWith("id,")) {
                continue;
            }
            Optional<Transaction> parsed = Transaction.parse(line);
            if (parsed.isPresent()) {
                transactions.add(parsed.get());
            } else {
                rejected.add("line " + lineNumber + ": " + line);
            }
        }
    }

    public List<Transaction> all() {
        return List.copyOf(transactions);
    }

    public List<String> rejected() {
        return rejected;
    }

    /** The transaction with this id, if there is one. The caller is forced to think about "no". */
    public Optional<Transaction> findById(String id) {
        return transactions.stream().filter(t -> t.id().equals(id)).findFirst();
    }

    /** The largest single deposit, if there were any deposits at all. */
    public Optional<Transaction> largestDeposit() {
        return transactions.stream()
                .filter(t -> t.kind() == Kind.DEPOSIT)
                .max(Comparator.comparing(Transaction::amount));
    }

    /** The most recent transaction for an account, if the account has any. */
    public Optional<Transaction> latestFor(String account) {
        return transactions.stream()
                .filter(t -> t.account().equals(account))
                .max(Comparator.naturalOrder());           // uses Transaction.compareTo, which is by date
    }

    /** One row per account, accounts in alphabetical order. */
    public List<AccountSummary> summaries() {
        Map<String, List<Transaction>> byAccount = new TreeMap<>();
        for (Transaction t : transactions) {
            byAccount.computeIfAbsent(t.account(), key -> new ArrayList<>()).add(t);
        }
        List<AccountSummary> rows = new ArrayList<>();
        for (Map.Entry<String, List<Transaction>> entry : byAccount.entrySet()) {
            BigDecimal in = BigDecimal.ZERO.setScale(2);
            BigDecimal out = BigDecimal.ZERO.setScale(2);
            for (Transaction t : entry.getValue()) {
                if (t.kind() == Kind.DEPOSIT) {
                    in = in.add(t.amount());
                } else {
                    out = out.add(t.amount());
                }
            }
            rows.add(new AccountSummary(entry.getKey(), entry.getValue().size(), in, out));
        }
        return rows;
    }
}

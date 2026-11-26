package com.codechanakya.reports;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Groups transactions by day, week and month. */
public class DailyReport {

    private static final LocalTime LATE = LocalTime.of(22, 0);

    private final List<Transaction> transactions;

    public DailyReport(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /** Net movement (deposits minus withdrawals) for each date. TreeMap keeps the dates in calendar order. */
    public Map<LocalDate, BigDecimal> netByDay() {
        Map<LocalDate, BigDecimal> net = new TreeMap<>();
        for (Transaction tx : transactions) {
            net.merge(tx.getDate(), signed(tx), BigDecimal::add);
        }
        return net;
    }

    /** Net movement for each week, keyed by the Monday that starts it. */
    public Map<LocalDate, BigDecimal> netByWeek() {
        Map<LocalDate, BigDecimal> net = new TreeMap<>();
        for (Transaction tx : transactions) {
            LocalDate monday = tx.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            net.merge(monday, signed(tx), BigDecimal::add);
        }
        return net;
    }

    /** Net movement for each calendar month. YearMonth is "March 2026", with no day. */
    public Map<YearMonth, BigDecimal> netByMonth() {
        Map<YearMonth, BigDecimal> net = new TreeMap<>();
        for (Transaction tx : transactions) {
            net.merge(YearMonth.from(tx.getDate()), signed(tx), BigDecimal::add);
        }
        return net;
    }

    /** Transactions made after 22:00, which a fraud team may want to look at. */
    public List<Transaction> lateNight() {
        List<Transaction> late = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.getTime().isAfter(LATE)) {
                late.add(tx);
            }
        }
        return late;
    }

    public int countOn(LocalDate date) {
        int count = 0;
        for (Transaction tx : transactions) {
            if (tx.getDate().equals(date)) {
                count++;
            }
        }
        return count;
    }

    private BigDecimal signed(Transaction tx) {
        return tx.getType() == TxType.DEPOSIT ? tx.getAmount() : tx.getAmount().negate();
    }

    /** I2: the date with the most transactions. Ties go to the earlier date. */
    public LocalDate busiestDay() {
        Map<LocalDate, Integer> counts = new TreeMap<>();
        for (Transaction tx : transactions) {
            counts.merge(tx.getDate(), 1, Integer::sum);
        }
        LocalDate best = null;
        int bestCount = 0;
        for (Map.Entry<LocalDate, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > bestCount) {
                best = entry.getKey();
                bestCount = entry.getValue();
            }
        }
        return best;
    }
}

package com.codechanakya.library;

import java.math.BigDecimal;

/** The two kinds of member and what each is allowed. Given to you: you do not change this file. */
public enum MemberType {

    STUDENT(14, 3, "2.00"),
    FACULTY(30, 10, "1.00");

    private final int loanDays;
    private final int maxLoans;
    private final BigDecimal finePerDay;

    MemberType(int loanDays, int maxLoans, String finePerDay) {
        this.loanDays = loanDays;
        this.maxLoans = maxLoans;
        this.finePerDay = new BigDecimal(finePerDay);
    }

    /** How many days a loan lasts. The due date is the borrowing date plus this. */
    public int loanDays() {
        return loanDays;
    }

    /** How many books a member may hold at the same time. */
    public int maxLoans() {
        return maxLoans;
    }

    /** The fine for each day a book is returned after its due date. */
    public BigDecimal finePerDay() {
        return finePerDay;
    }
}

package com.codechanakya.hr;

/** The kinds of leave and how many working days a year each allows. {@code -1} means no limit. */
public enum LeaveType {
    ANNUAL(18), SICK(10), UNPAID(-1);

    private final int yearlyAllowance;

    LeaveType(int yearlyAllowance) {
        this.yearlyAllowance = yearlyAllowance;
    }

    public int yearlyAllowance() {
        return yearlyAllowance;
    }

    public boolean hasLimit() {
        return yearlyAllowance >= 0;
    }
}

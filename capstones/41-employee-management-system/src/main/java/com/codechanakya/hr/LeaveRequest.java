package com.codechanakya.hr;

import java.time.LocalDate;

/** A request for leave from {@code from} to {@code to}, both days included. {@code days} counts working days only. */
public record LeaveRequest(int id, int employeeId, LeaveType type, LocalDate from, LocalDate to, int days,
                           LeaveStatus status) {

    public boolean overlaps(LocalDate otherFrom, LocalDate otherTo) {
        return !from.isAfter(otherTo) && !to.isBefore(otherFrom);
    }

    public LeaveRequest withStatus(LeaveStatus newStatus) {
        return new LeaveRequest(id, employeeId, type, from, to, days, newStatus);
    }
}

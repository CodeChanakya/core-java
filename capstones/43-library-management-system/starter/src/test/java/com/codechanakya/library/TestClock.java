package com.codechanakya.library;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** A clock that only moves when a test says so. Given to you: do not change it. */
final class TestClock extends Clock {

    private LocalDate today;

    TestClock(LocalDate start) {
        this.today = start;
    }

    void advance(int days) {
        today = today.plusDays(days);
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return today.atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}

package com.codechanakya.hr;

import java.math.BigDecimal;
import java.time.YearMonth;

/** What one employee is paid for one month. */
public record Payslip(int employeeId, YearMonth month, BigDecimal gross, int unpaidDays, BigDecimal unpaidDeduction,
                      BigDecimal tax, BigDecimal net) {
}

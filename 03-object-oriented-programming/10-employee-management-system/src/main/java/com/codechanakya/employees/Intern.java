package com.codechanakya.employees;

import java.math.BigDecimal;

/**
 * An intern is paid a fixed stipend. The programme sets it, so raises do not
 * apply. This is the reason applyRaise is overridable: one subclass can change
 * a rule without any other class knowing.
 */
public class Intern extends Employee {

    public Intern(String id, String name, BigDecimal stipend) {
        super(id, name, stipend);
    }

    @Override
    public void applyRaise(BigDecimal percent) {
        // Deliberately empty: the stipend does not change.
    }

    @Override
    public String role() {
        return "Intern";
    }
}

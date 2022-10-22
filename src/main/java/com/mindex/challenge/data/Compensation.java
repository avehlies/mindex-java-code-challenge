package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Compensation {
    private Employee employee;
    private LocalDate effectiveDate;
    private BigDecimal salary;

    public Compensation() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public Compensation setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Compensation setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Compensation setSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }
}

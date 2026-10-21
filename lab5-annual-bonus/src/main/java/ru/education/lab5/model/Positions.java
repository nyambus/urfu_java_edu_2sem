package ru.education.lab5.model;

import java.math.BigDecimal;

public enum Positions {
    DEV("2.2", false), HR("1.2", false), TL("2.6", true),
    PO("2.4", true), TPM("2.8", true), CTO("3.0", true);

    private final BigDecimal coefficient;
    private final boolean manager;

    Positions(String coefficient, boolean manager) {
        this.coefficient = new BigDecimal(coefficient);
        this.manager = manager;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public boolean isManager() {
        return manager;
    }
}

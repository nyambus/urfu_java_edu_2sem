package ru.education.lab5.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import ru.education.lab5.model.Positions;

public interface AnnualBonusService {
    BigDecimal calculate(BigDecimal salary, BigDecimal bonus, int workDays,
                         Positions position, LocalDate date);

    /** Only management positions are eligible; others cause IllegalArgumentException. */
    BigDecimal calculateQuarterly(BigDecimal salary, BigDecimal bonus, int workDays,
                                  Positions position, LocalDate date);
}

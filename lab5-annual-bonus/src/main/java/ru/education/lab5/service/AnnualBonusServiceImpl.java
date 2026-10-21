package ru.education.lab5.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Objects;
import org.springframework.stereotype.Service;
import ru.education.lab5.model.Positions;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {
    @Override
    public BigDecimal calculate(BigDecimal salary, BigDecimal bonus, int workDays,
                                Positions position, LocalDate date) {
        validate(salary, bonus, workDays, position, date);
        int daysInYear = Year.isLeap(date.getYear()) ? 366 : 365;
        return amount(salary, bonus, workDays, position, daysInYear);
    }

    @Override
    public BigDecimal calculateQuarterly(BigDecimal salary, BigDecimal bonus, int workDays,
                                         Positions position, LocalDate date) {
        validate(salary, bonus, workDays, position, date);
        if (!position.isManager()) {
            throw new IllegalArgumentException("Quarterly bonus is only available for managers");
        }
        int firstMonth = ((date.getMonthValue() - 1) / 3) * 3 + 1;
        int daysInQuarter = YearMonth.of(date.getYear(), firstMonth).lengthOfMonth()
                + YearMonth.of(date.getYear(), firstMonth + 1).lengthOfMonth()
                + YearMonth.of(date.getYear(), firstMonth + 2).lengthOfMonth();
        return amount(salary, bonus, workDays, position, daysInQuarter);
    }

    private static void validate(BigDecimal salary, BigDecimal bonus, int workDays,
                                 Positions position, LocalDate date) {
        Objects.requireNonNull(salary, "salary");
        Objects.requireNonNull(bonus, "bonus");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(date, "date");
        if (salary.signum() < 0 || bonus.signum() < 0 || workDays <= 0) {
            throw new IllegalArgumentException("salary and bonus must be nonnegative; workDays must be positive");
        }
    }

    private static BigDecimal amount(BigDecimal salary, BigDecimal bonus, int workDays,
                                     Positions position, int days) {
        return salary.multiply(bonus).multiply(BigDecimal.valueOf(days))
                .multiply(position.getCoefficient())
                .divide(BigDecimal.valueOf(workDays), 2, RoundingMode.HALF_UP);
    }
}

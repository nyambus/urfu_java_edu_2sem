package ru.education.lab5.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.education.lab5.model.Positions;

class AnnualBonusServiceImplTest {
    private final AnnualBonusService service = new AnnualBonusServiceImpl();
    private final BigDecimal salary = new BigDecimal("1000");
    private final BigDecimal bonus = new BigDecimal("0.1");

    @Test
    void annualUsesActualGregorianYearAndPositionCoefficient() {
        assertEquals(new BigDecimal("220.00"), service.calculate(salary, bonus, 365,
                Positions.DEV, LocalDate.of(2023, 12, 31)));
        assertEquals(new BigDecimal("220.60"), service.calculate(salary, bonus, 365,
                Positions.DEV, LocalDate.of(2024, 2, 29)));
        assertEquals(new BigDecimal("220.00"), service.calculate(salary, bonus, 365,
                Positions.DEV, LocalDate.of(1900, 3, 1)));
        assertEquals(new BigDecimal("220.60"), service.calculate(salary, bonus, 365,
                Positions.DEV, LocalDate.of(2000, 3, 1)));
    }

    @Test
    void quarterlyCountsExactDaysAndAppliesManagementCoefficient() {
        assertEquals(new BigDecimal("2340.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.TL, LocalDate.of(2023, 3, 31)));
        assertEquals(new BigDecimal("2366.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.TL, LocalDate.of(2024, 2, 29)));
        assertEquals(new BigDecimal("2366.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.TL, LocalDate.of(2024, 4, 1)));
        assertEquals(new BigDecimal("2160.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.PO, LocalDate.of(2023, 3, 31)));
        assertEquals(new BigDecimal("2520.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.TPM, LocalDate.of(2023, 3, 31)));
        assertEquals(new BigDecimal("2760.00"), service.calculateQuarterly(salary, bonus, 10,
                Positions.CTO, LocalDate.of(2024, 12, 31)));
    }

    @Test
    void nonManagersCanGetAnnualButNeverQuarterly() {
        assertEquals(new BigDecimal("120.00"), service.calculate(salary, bonus, 365,
                Positions.HR, LocalDate.of(2023, 1, 1)));
        for (Positions position : new Positions[] {Positions.DEV, Positions.HR}) {
            assertThrows(IllegalArgumentException.class, () -> service.calculateQuarterly(
                    salary, bonus, 365, position, LocalDate.of(2024, 1, 1)));
        }
    }

    @Test
    void invalidPayInputsAreRejectedAndZeroIsAllowed() {
        assertThrows(IllegalArgumentException.class, () -> service.calculate(salary, bonus, 0,
                Positions.DEV, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.calculate(new BigDecimal("-1"), bonus, 10,
                Positions.DEV, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.calculateQuarterly(salary,
                new BigDecimal("-0.1"), 10, Positions.TL, LocalDate.of(2024, 1, 1)));
        assertEquals(new BigDecimal("0.00"), service.calculate(BigDecimal.ZERO, bonus, 365,
                Positions.DEV, LocalDate.of(2024, 1, 1)));
    }
}

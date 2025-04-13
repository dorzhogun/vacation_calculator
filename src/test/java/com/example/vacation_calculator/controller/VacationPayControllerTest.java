package com.example.vacation_calculator.controller;

import com.example.vacation_calculator.component.HolidayCalendar;
import com.example.vacation_calculator.exception.AverageSalaryException;
import com.example.vacation_calculator.exception.StartDateErrorException;
import com.example.vacation_calculator.exception.VacationDaysException;
import com.example.vacation_calculator.service.impl.VacationPayServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VacationPayControllerTest {
    @Autowired
    protected HolidayCalendar holidayCalendar;

    @Autowired
    protected VacationPayServiceImpl vacationPayService;

    @Test
    void when_getVacationPay_WithoutStartDate_thenReturnCorrectVacationPayAmount() {
        BigDecimal actual = vacationPayService.getVacationPay(BigDecimal.valueOf(30000), 10, null);
        assertEquals(BigDecimal.valueOf(10238.90).setScale(2, RoundingMode.HALF_UP),
                actual.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void when_getVacationPay_WithStartDate_thenReturnCorrectPayAmountWithoutHolidaysAndWeekends() {
        LocalDate startDate = LocalDate.of(2025, 7, 1);
        BigDecimal actual = vacationPayService.getVacationPay(
                BigDecimal.valueOf(29300), 14, startDate).setScale(2, RoundingMode.HALF_UP);
        assertEquals(BigDecimal.valueOf(10000.00).setScale(2, RoundingMode.HALF_UP), actual);
    }

    @Test
    void isHolidayOrWeekend_Weekend_ShouldReturnTrue() {
        // 12 июня - праздник
        assertTrue(holidayCalendar.isHolidayOrWeekend(LocalDate.of(2025, 6, 12)));
        // Суббота и Воскресенье
        assertTrue(holidayCalendar.isHolidayOrWeekend(LocalDate.of(2025, 6, 7)));
        assertTrue(holidayCalendar.isHolidayOrWeekend(LocalDate.of(2025, 6, 8)));
        assertTrue(holidayCalendar.isHolidayOrWeekend(LocalDate.of(2025, 6, 14)));
    }

    @Test
    void when_getVacationPay_WithStartDateOnHolidays_thenReturnCorrectPayAmountWithoutHolidaysAndWeekends() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);

        BigDecimal actual = vacationPayService.getVacationPay(
                BigDecimal.valueOf(29300), 14, startDate).setScale(2, RoundingMode.HALF_UP);
        assertEquals(BigDecimal.valueOf(6000.00).setScale(2, RoundingMode.HALF_UP), actual);
    }

    @Test
    void getVacationPay_WithVacationDaysLessThan1_ShouldThrowException() {
        assertThrows(VacationDaysException.class,
                () -> vacationPayService.getVacationPay(BigDecimal.valueOf(30000), 0, null));
    }

    @Test
    void getVacationPay_WithSalaryLessThanMinimumWage_ShouldThrowException() {
        assertThrows(AverageSalaryException.class,
                () -> vacationPayService.getVacationPay(BigDecimal.valueOf(10000), 10, null));
    }

    @Test
    void getVacationPay_WithStartDateInPast_ShouldThrowException() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThrows(StartDateErrorException.class,
                () -> vacationPayService.getVacationPay(BigDecimal.valueOf(30000), 10, pastDate));
    }
}

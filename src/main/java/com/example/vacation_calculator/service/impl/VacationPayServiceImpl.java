package com.example.vacation_calculator.service.impl;

import com.example.vacation_calculator.component.HolidayCalendar;
import com.example.vacation_calculator.exception.AverageSalaryException;
import com.example.vacation_calculator.exception.StartDateErrorException;
import com.example.vacation_calculator.exception.VacationDaysException;
import com.example.vacation_calculator.service.VacationPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationPayServiceImpl implements VacationPayService {
    private static final BigDecimal AVERAGE_DAYS_IN_MONTH = BigDecimal.valueOf(29.3);
    private final HolidayCalendar holidayCalendar;
    private static final BigDecimal MINIMUM_WAGE = BigDecimal.valueOf(22440);

    @Override
    public BigDecimal getVacationPay(BigDecimal averageSalary, Integer vacationDays, LocalDate startDate) {
        if (vacationDays < 1) {
            log.info("Количество отпускных дней должно быть не менее 1!");
            throw new VacationDaysException("VacationDays can't be less 1!");
        }

        if (averageSalary.compareTo(MINIMUM_WAGE) < 0) {
            log.info("Среднемесячная зарплата должна быть не меньше МРОТ (22 440 руб.)");
            throw new AverageSalaryException("AverageSalary can't be less 22 440 RUR");
        }

        BigDecimal dailySalary = averageSalary.divide(AVERAGE_DAYS_IN_MONTH, 2, RoundingMode.HALF_UP);
        if (startDate == null) {
            return dailySalary.multiply(BigDecimal.valueOf(vacationDays));
        } else if (startDate.isBefore(LocalDate.now())) {
            log.info("Дата начала отпуска не может быть в прошлом");
            throw new StartDateErrorException("The startDate can't be in the past!");
        } else {
            int workingDays = calculateWorkingDays(startDate, vacationDays);
            return dailySalary.multiply(BigDecimal.valueOf(workingDays));
        }
    }

    private int calculateWorkingDays(LocalDate startDate, int vacationDays) {
        int workingDays = 0;
        LocalDate currentDate = startDate;
        for (int i = 0; i < vacationDays; i++) {
            if (!holidayCalendar.isHolidayOrWeekend(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workingDays;
    }
}

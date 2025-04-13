package com.example.vacation_calculator.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VacationPayService {
    BigDecimal getVacationPay(BigDecimal averageSalary, Integer vacationDays, LocalDate startDate);
}

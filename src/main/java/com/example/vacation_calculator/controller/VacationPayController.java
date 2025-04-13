package com.example.vacation_calculator.controller;

import com.example.vacation_calculator.service.VacationPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Калькулятор отпускных", description = "API для расчёта суммы отпускных")
public class VacationPayController {
    private final VacationPayService vacationPayService;

    @Operation(
            summary = "Получить расчёт суммы отпускных на основе среднемесячной зарплаты и количества дней отпуска",
            description = "Возвращает сумму отпускных по стандартам финансовых расчётов с общепринятыми правилами " +
                    "округления, чтобы сотруник не терял копейки. Для учёта в расчёте праздничных дней и выходных " +
                    "необходимо дополнительно указать точную дату ухода в отпуск",
            tags = {"Get"}
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/calculate")
    public BigDecimal calculateVacationPay(
            @RequestParam @NotNull BigDecimal averageSalary,
            @RequestParam @NotNull Integer vacationDays,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate)
    {
        return vacationPayService.getVacationPay(averageSalary, vacationDays, startDate);
    }
}

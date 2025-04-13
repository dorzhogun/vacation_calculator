package com.example.vacation_calculator.component;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Component
public class HolidayCalendar {
    private final Set<LocalDate> holidays;

    public HolidayCalendar() {
        this.holidays = initializeHolidays();
    }

    public boolean isHolidayOrWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY
                || holidays.contains(date);
    }

    private Set<LocalDate> initializeHolidays() {
        Set<LocalDate> holidays = new HashSet<>();

        // Праздничные дни 2025 года (по производственному календарю РФ)
        // Январь
        holidays.add(LocalDate.of(2025, Month.JANUARY, 1));
        holidays.add(LocalDate.of(2025, Month.JANUARY, 2));
        holidays.add(LocalDate.of(2025, Month.JANUARY, 3));
        holidays.add(LocalDate.of(2025, Month.JANUARY, 6));
        holidays.add(LocalDate.of(2025, Month.JANUARY, 7));
        holidays.add(LocalDate.of(2025, Month.JANUARY, 8));

        // Февраль
        holidays.add(LocalDate.of(2025, Month.FEBRUARY, 23));
        holidays.add(LocalDate.of(2025, Month.FEBRUARY, 24)); // перенос

        // Март
        holidays.add(LocalDate.of(2025, Month.MARCH, 8));
        holidays.add(LocalDate.of(2025, Month.MARCH, 10)); // перенос

        // Май
        holidays.add(LocalDate.of(2025, Month.MAY, 1));
        holidays.add(LocalDate.of(2025, Month.MAY, 2));
        holidays.add(LocalDate.of(2025, Month.MAY, 9));
        holidays.add(LocalDate.of(2025, Month.MAY, 12)); // перенос

        // Июнь
        holidays.add(LocalDate.of(2025, Month.JUNE, 12));

        // Ноябрь
        holidays.add(LocalDate.of(2025, Month.NOVEMBER, 4));

        return holidays;
    }
}

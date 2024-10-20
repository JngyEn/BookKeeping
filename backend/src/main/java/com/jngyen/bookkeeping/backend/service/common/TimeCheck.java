package com.jngyen.bookkeeping.backend.service.common;

import java.time.LocalDate;

public class TimeCheck {
    public static boolean isTimeRangeValid(LocalDate startDate, LocalDate endDate) {
        return !startDate.isBefore(endDate);
    }

}

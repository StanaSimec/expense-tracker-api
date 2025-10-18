package com.simec.expense_tracker_api.date;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateWrapperImpl implements DateWrapper {

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }

    @Override
    public LocalDate parse(String date) {
        return LocalDate.parse(date);
    }
}

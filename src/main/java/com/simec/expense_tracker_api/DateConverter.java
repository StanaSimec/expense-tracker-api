package com.simec.expense_tracker_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DateConverter {

    private final String timezone;

    @Autowired
    public DateConverter(@Value("${spring.application.timezone}") String timezone) {
        this.timezone = timezone;
    }

    public Instant toInstant(LocalDate date) {
        return date.atStartOfDay().atZone(ZoneId.of(timezone)).toInstant();
    }

    public LocalDate toLocalDate(Instant instant) {
        Instant.now().atZone(ZoneId.of("Europe/Prague")).toLocalDate();
        return LocalDate.ofInstant(instant, ZoneId.of(timezone));
    }

}

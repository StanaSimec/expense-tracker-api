package com.simec.expense_tracker_api;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateConverterTest {
    private final DateConverter dateConverter = new DateConverter("Europe/Paris");

    @Test
    void whenIsLocalDateConvertedThenIsEqualsToInstant() {
        LocalDate localDate = LocalDate.of(2021, 3, 2);
        Instant instant = dateConverter.toInstant(localDate);
        assertEquals(instant, Instant.parse("2021-03-01T23:00:00.00Z"));
    }

    @Test
    void whenIsInstantConvertedThenIsEqualsToLocalDate() {
        Instant instant = Instant.parse("2023-02-01T18:55:19.061705Z");
        LocalDate localDate = dateConverter.toLocalDate(instant);
        assertEquals(LocalDate.of(2023, 2, 1), localDate);
    }
}
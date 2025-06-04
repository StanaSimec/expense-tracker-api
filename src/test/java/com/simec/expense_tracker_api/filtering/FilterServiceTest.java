package com.simec.expense_tracker_api.filtering;

import com.simec.expense_tracker_api.exception.FilterNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterServiceTest {

    private static final FilterService filterService = new FilterService();
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 1);
    private static final String WEEK = "week";
    private static final String MONTH = "month";
    private static final String THREE_MONTHS = "three_months";

    @Test
    void whenLastWeekTagIsUsedThenStartAndEndAreCorrect() {
        Filter filter = filterService.getForTag(WEEK, END_DATE);
        assertFilter(filter, END_DATE.minusWeeks(1));
    }

    @Test
    void whenLastMonthTagIsUsedThenStartAndEndAreCorrect() {
        Filter filter = filterService.getForTag(MONTH, END_DATE);
        assertFilter(filter, END_DATE.minusMonths(1));
    }

    @Test
    void whenLastThreeMonthTagIsUsedThenStartAndEndAreCorrect() {
        Filter filter = filterService.getForTag(THREE_MONTHS, END_DATE);
        assertFilter(filter, END_DATE.minusMonths(3));
    }

    @Test
    void whenInvalidTagIsUsedThenExceptionIsThrown() {
        assertThrows(FilterNotFoundException.class, () -> filterService.getForTag("invalid", END_DATE));
    }

    @Test
    void whenAllTagsIsCalledThenIsCorrect() {
        List<String> tags = filterService.getAllTags();
        assertTrue(tags.contains(WEEK));
        assertTrue(tags.contains(MONTH));
        assertTrue(tags.contains(THREE_MONTHS));
    }

    private void assertFilter(Filter filter, LocalDate start) {
        assertEquals(start, filter.start());
        assertEquals(END_DATE, filter.end());
    }
}
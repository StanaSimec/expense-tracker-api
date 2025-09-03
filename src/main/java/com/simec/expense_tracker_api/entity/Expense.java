package com.simec.expense_tracker_api.entity;

import java.time.LocalDate;

public record Expense(
        long id,
        String name,
        LocalDate appliedAt,
        Category category
) {
}

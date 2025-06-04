package com.simec.expense_tracker_api.entity;

import java.time.Instant;

public record Expense(
        long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        Instant appliedAt,
        Category category
) {
}

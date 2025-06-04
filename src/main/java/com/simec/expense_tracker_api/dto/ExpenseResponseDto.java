package com.simec.expense_tracker_api.dto;

import java.time.LocalDate;

public record ExpenseResponseDto(
        long id,
        String name,
        LocalDate createdAt,
        LocalDate updatedAt,
        LocalDate appliedAt,
        String category
) {
}

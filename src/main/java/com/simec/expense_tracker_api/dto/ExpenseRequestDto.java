package com.simec.expense_tracker_api.dto;

import com.simec.expense_tracker_api.validation.CategoryExists;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ExpenseRequestDto(
        @NotBlank(message = "Name is required")
        @Size(max = 250, message = "Name should be shorter than 250 characters")
        String name,

        @NotNull(message = "Applied at is required")
        LocalDate appliedAt,

        @NotBlank(message = "Category is required")
        @Size(max = 50, message = "Category should be shorter than 50 characters")
        @CategoryExists
        String category) {
}

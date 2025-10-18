package com.simec.expense_tracker_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequestDto(
        @NotBlank(message = "email is required")
        @Email(message = "invalid email format")
        String email,
        @NotBlank(message = "email is required")
        @Length(max = 100, message = "password cannot be longed than 100 characters")
        String password) {
}

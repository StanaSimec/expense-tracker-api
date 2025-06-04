package com.simec.expense_tracker_api.dto;

import java.util.List;

public record ErrorListResponseDto(List<String> messages) {
}

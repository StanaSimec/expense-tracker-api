package com.simec.expense_tracker_api.filtering;

import java.time.LocalDate;

public record Filter(LocalDate start, LocalDate end) {
}

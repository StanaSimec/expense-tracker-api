package com.simec.expense_tracker_api.date;

import java.time.LocalDate;

public interface DateWrapper {

    LocalDate now();

    LocalDate parse(String date);
}

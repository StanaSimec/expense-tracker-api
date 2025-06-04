package com.simec.expense_tracker_api.filtering;

enum FilterType {
    LAST_WEEK("week"),
    LAST_MONTH("month"),
    LAST_THREE_MONTHS("three_months");

    private final String tag;

    FilterType(String tag) {
        this.tag = tag;
    }

    String getTag() {
        return tag;
    }
}

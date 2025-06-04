package com.simec.expense_tracker_api.exception;

public class FilterNotFoundException extends RuntimeException {
    public FilterNotFoundException(String message) {
        super(message);
    }
}

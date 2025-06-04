package com.simec.expense_tracker_api.service;

import com.simec.expense_tracker_api.dto.ExpenseRequestDto;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.filtering.Filter;

import java.util.List;

public interface ExpenseService {
    Expense create(ExpenseRequestDto dto);

    Expense find(long id);

    void delete(long id);

    Expense update(ExpenseRequestDto requestDto, long expenseId);

    List<Expense> findByFilter(Filter filter);

    List<Expense> findAll();
}

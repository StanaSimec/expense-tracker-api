package com.simec.expense_tracker_api.dao;

import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.filtering.Filter;

import java.util.List;
import java.util.Optional;

public interface ExpenseDao {
    Optional<Expense> find(long expenseId, long personId);

    void delete(long expenseId, long personId);

    void update(Expense expense, long personId);

    long create(Expense expense, long personId);

    List<Expense> findByFilter(Filter filter, long personId);

    List<Expense> findAll(long personId);
}

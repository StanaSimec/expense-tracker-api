package com.simec.expense_tracker_api.service;

import com.simec.expense_tracker_api.Authentication;
import com.simec.expense_tracker_api.dao.CategoryDao;
import com.simec.expense_tracker_api.dao.ExpenseDao;
import com.simec.expense_tracker_api.dao.UserDao;
import com.simec.expense_tracker_api.dto.ExpenseRequestDto;
import com.simec.expense_tracker_api.entity.Category;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.exception.CategoryNotFoundException;
import com.simec.expense_tracker_api.exception.ExpenseNotFoundException;
import com.simec.expense_tracker_api.filtering.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseDao expenseDao;
    private final CategoryDao categoryDao;
    private final Authentication authentication;

    @Autowired
    public ExpenseServiceImpl(ExpenseDao expenseDao, CategoryDao categoryDao, Authentication authentication,
                              UserDao userDao) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.authentication = authentication;
    }

    @Override
    public Expense create(ExpenseRequestDto dto) {
        Category category = findCategory(dto.category());
        Expense expense = new Expense(
                authentication.getPrincipalId(),
                dto.name(),
                dto.appliedAt(),
                category
        );
        long createdId = expenseDao.create(expense, authentication.getPrincipalId());
        return find(createdId);
    }

    @Override
    public Expense find(long id) {
        return expenseDao.find(id, authentication.getPrincipalId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
    }

    @Override
    public void delete(long id) {
        Expense expense = find(id);
        expenseDao.delete(expense.id(), authentication.getPrincipalId());
    }

    @Override
    public Expense update(ExpenseRequestDto requestDto, long expenseId) {
        Expense savedExpense = find(expenseId);
        Category category = findCategory(requestDto.category());
        Expense updatedExpense = new Expense(
                savedExpense.id(),
                requestDto.name(),
                requestDto.appliedAt(),
                category
        );
        expenseDao.update(updatedExpense, authentication.getPrincipalId());
        return find(savedExpense.id());
    }

    @Override
    public List<Expense> findByFilter(Filter filter) {
        return expenseDao.findByFilter(filter, authentication.getPrincipalId());
    }

    @Override
    public List<Expense> findAll() {
        return expenseDao.findAll(authentication.getPrincipalId());
    }

    private Category findCategory(String name) {
        return categoryDao.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }
}

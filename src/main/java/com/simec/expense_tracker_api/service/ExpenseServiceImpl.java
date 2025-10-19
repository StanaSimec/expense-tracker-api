package com.simec.expense_tracker_api.service;

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
    private final AuthenticationService authenticationService;

    @Autowired
    public ExpenseServiceImpl(ExpenseDao expenseDao, CategoryDao categoryDao, AuthenticationService authenticationService,
                              UserDao userDao) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.authenticationService = authenticationService;
    }

    @Override
    public Expense create(ExpenseRequestDto dto) {
        Category category = findCategory(dto.category());
        Expense expense = new Expense(
                authenticationService.getPrincipalId(),
                dto.name(),
                dto.appliedAt(),
                category
        );
        long createdId = expenseDao.create(expense, authenticationService.getPrincipalId());
        return find(createdId);
    }

    @Override
    public Expense find(long id) {
        return expenseDao.find(id, authenticationService.getPrincipalId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
    }

    @Override
    public void delete(long id) {
        Expense expense = find(id);
        expenseDao.delete(expense.id(), authenticationService.getPrincipalId());
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
        expenseDao.update(updatedExpense, authenticationService.getPrincipalId());
        return find(savedExpense.id());
    }

    @Override
    public List<Expense> findByFilter(Filter filter) {
        return expenseDao.findByFilter(filter, authenticationService.getPrincipalId());
    }

    @Override
    public List<Expense> findAll() {
        return expenseDao.findAll(authenticationService.getPrincipalId());
    }

    private Category findCategory(String name) {
        return categoryDao.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }
}

package com.simec.expense_tracker_api.service;

import com.simec.expense_tracker_api.dao.CategoryDao;
import com.simec.expense_tracker_api.dao.ExpenseDao;
import com.simec.expense_tracker_api.dto.ExpenseRequestDto;
import com.simec.expense_tracker_api.entity.Category;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.exception.ExpenseNotFoundException;
import com.simec.expense_tracker_api.filtering.Filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    private static final Category CATEGORY = new Category(1L, "Sport");
    private static final LocalDate DATE_NOW = LocalDate.of(2023, 12, 31);
    private static final Expense EXPENSE = new Expense(
            1L,
            "Run a marathon",
            DATE_NOW,
            CATEGORY
    );
    private static final long PRINCIPAL_ID = 1L;
    private static final Filter FILTER = new Filter(DATE_NOW.minusWeeks(2), DATE_NOW);

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private ExpenseDao expenseDao;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        Mockito.when(authenticationService.getPrincipalId()).thenReturn(PRINCIPAL_ID);
    }

    @Test
    void whenExpenseExistsThenIsExpenseReturned() {
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        Assertions.assertEquals(EXPENSE, expenseService.find(EXPENSE.id()));
    }

    @Test
    void whenExpenseDoesNotExistThenIsExceptionThrown() {
        long expenseId = 3L;
        Mockito.when(expenseDao.find(expenseId, PRINCIPAL_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(ExpenseNotFoundException.class, () -> expenseService.find(expenseId));
    }

    @Test
    void whenExpenseExistsThenIsExpenseDeleted() {
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        expenseService.delete(EXPENSE.id());
        Mockito.verify(expenseDao, Mockito.times(1)).delete(EXPENSE.id(), PRINCIPAL_ID);
    }

    @Test
    void whenExpenseDoesNotExistsThenIsNotDeletedAndExceptionIsThrown() {
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(ExpenseNotFoundException.class, () -> expenseService.delete(EXPENSE.id()));
        Mockito.verify(expenseDao, Mockito.times(0)).delete(EXPENSE.id(), PRINCIPAL_ID);
    }

    @Test
    void whenFilteredListIsFilledThenIsFilledListReturned() {
        Mockito.when(expenseDao.findByFilter(FILTER, PRINCIPAL_ID)).thenReturn(List.of(EXPENSE));
        Assertions.assertEquals(List.of(EXPENSE), expenseService.findByFilter(FILTER));
    }

    @Test
    void whenFilteredListIsEmptyThenIsEmptyListReturned() {
        Mockito.when(expenseDao.findByFilter(FILTER, PRINCIPAL_ID)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), expenseService.findByFilter(FILTER));
    }

    @Test
    void whenListIsFilledThenIsFilledListReturned() {
        Mockito.when(expenseDao.findAll(PRINCIPAL_ID)).thenReturn(List.of(EXPENSE));
        Assertions.assertEquals(List.of(EXPENSE), expenseService.findAll());
    }

    @Test
    void whenListIsEmptyThenIsEmptyListReturned() {
        Mockito.when(expenseDao.findAll(PRINCIPAL_ID)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), expenseService.findAll());
    }

    @Test
    void whenExpenseIsCreatedDaoIsCalled() {
        Expense expense = new Expense(0L, "testing", DATE_NOW, CATEGORY);
        Mockito.when(categoryDao.findByName(CATEGORY.name())).thenReturn(Optional.of(CATEGORY));
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        Mockito.when(expenseDao.create(Mockito.eq(expense), Mockito.eq(PRINCIPAL_ID))).thenReturn(EXPENSE.id());

        expenseService.create(new ExpenseRequestDto(
                expense.name(),
                DATE_NOW,
                CATEGORY.name()
        ));

        Mockito.verify(expenseDao, Mockito.times(1))
                .create(Mockito.eq(expense), Mockito.eq(PRINCIPAL_ID));
    }

    @Test
    void whenExpenseIsCreatedThenNewExpenseIsReturned() {
        Mockito.when(categoryDao.findByName(CATEGORY.name())).thenReturn(Optional.of(CATEGORY));
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        Mockito.when(expenseDao.create(Mockito.any(Expense.class), Mockito.anyLong())).thenReturn(EXPENSE.id());

        Expense createdExpense = expenseService.create(new ExpenseRequestDto(
                EXPENSE.name(),
                DATE_NOW,
                CATEGORY.name()
        ));

        Assertions.assertEquals(EXPENSE, createdExpense);
    }

    @Test
    void whenExpenseIsUpdatedThenDaoIsCalled() {
        Expense expense = new Expense(
                EXPENSE.id(),
                "Updated Name",
                DATE_NOW,
                CATEGORY);
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        Mockito.when(categoryDao.findByName(CATEGORY.name())).thenReturn(Optional.of(CATEGORY));

        expenseService.update(new ExpenseRequestDto(expense.name(), DATE_NOW, CATEGORY.name()),expense.id());

        Mockito.verify(expenseDao, Mockito.times(1)).update(Mockito.eq(expense),
                Mockito.eq(PRINCIPAL_ID));
    }

    @Test
    void whenExpenseIsUpdatedThenUpdatedExpenseIsReturned() {
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.of(EXPENSE));
        Mockito.when(categoryDao.findByName(CATEGORY.name())).thenReturn(Optional.of(CATEGORY));

        Expense updatedExpense = expenseService.update(
                new ExpenseRequestDto(EXPENSE.name(), DATE_NOW, CATEGORY.name()), EXPENSE.id());
        Assertions.assertEquals(EXPENSE, updatedExpense);
    }

    @Test
    void whenExpenseDoesNotExitsThenExceptionIsThrown() {
        Mockito.when(expenseDao.find(EXPENSE.id(), PRINCIPAL_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ExpenseNotFoundException.class,
                () -> expenseService.update(
                        new ExpenseRequestDto(EXPENSE.name(), DATE_NOW, CATEGORY.name()),
                        EXPENSE.id())
        );
    }
}
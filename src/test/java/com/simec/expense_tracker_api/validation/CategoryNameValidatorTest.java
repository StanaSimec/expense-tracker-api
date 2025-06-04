package com.simec.expense_tracker_api.validation;

import com.simec.expense_tracker_api.dao.CategoryDao;
import com.simec.expense_tracker_api.entity.Category;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CategoryNameValidatorTest {
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    private final Category category = new Category(1, "Electronics");
    private CategoryNameValidator categoryNameValidator;
    private CategoryDao categoryDao;

    @BeforeEach
    void setUp() {
        categoryDao = mock(CategoryDao.class);
        categoryNameValidator = new CategoryNameValidator(categoryDao);
    }

    @Test
    void whenCategoryExistsThenIsValid() {
        when(categoryDao.findByName(category.name())).thenReturn(Optional.of(category));
        assertTrue(categoryNameValidator.isValid(category.name(), context));
        verifyNoInteractions(context);
    }

    @Test
    void whenCategoryDoesNotExistThenIsInvalid() {
        String others = "Others";
        Mockito.when(categoryDao.findByName(others)).thenReturn(Optional.empty());
        assertFalse(categoryNameValidator.isValid(others, context));
        verifyNoInteractions(context);
    }

    @Test
    void whenCategoryIsNullThenIsInvalid() {
        assertFalse(categoryNameValidator.isValid(null, context));
        verifyNoInteractions(context);
    }
}
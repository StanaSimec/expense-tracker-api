package com.simec.expense_tracker_api.validation;

import com.simec.expense_tracker_api.dao.CategoryDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryNameValidator implements ConstraintValidator<CategoryExists, String> {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryNameValidator(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public boolean isValid(String categoryName, ConstraintValidatorContext context) {
        if (categoryName == null) {
            return false;
        }
        return categoryDao.findByName(categoryName).isPresent();
    }
}

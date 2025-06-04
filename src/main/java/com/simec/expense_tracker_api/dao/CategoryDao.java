package com.simec.expense_tracker_api.dao;

import com.simec.expense_tracker_api.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    List<Category> findAll();

    Optional<Category> findByName(String name);
}

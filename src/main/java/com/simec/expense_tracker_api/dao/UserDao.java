package com.simec.expense_tracker_api.dao;

import com.simec.expense_tracker_api.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByEmail(String email);

    User create(User user);

    boolean isEmailUnique(String email);
}

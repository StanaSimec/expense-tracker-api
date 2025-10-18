package com.simec.expense_tracker_api;

import com.simec.expense_tracker_api.dao.UserDao;
import com.simec.expense_tracker_api.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Authentication {

    private final UserDao userDao;

    @Autowired
    public Authentication(UserDao userDao) {
        this.userDao = userDao;
    }

    public long getPrincipalId() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();
        return userDao.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Invalid user.")).getId();
    }
}

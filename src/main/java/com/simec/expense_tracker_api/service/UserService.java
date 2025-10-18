package com.simec.expense_tracker_api.service;

import com.simec.expense_tracker_api.dto.LoginRequestDto;
import com.simec.expense_tracker_api.dto.RegisterRequestDto;
import com.simec.expense_tracker_api.entity.User;

public interface UserService {
    User register(RegisterRequestDto registerDto);

    User login(LoginRequestDto loginDto);
}

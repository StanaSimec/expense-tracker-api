package com.simec.expense_tracker_api.service;

public interface JwtService {
    String generateForEmail(String email);

    void validateToken(String token);

    String extractEmail(String jwt);
}

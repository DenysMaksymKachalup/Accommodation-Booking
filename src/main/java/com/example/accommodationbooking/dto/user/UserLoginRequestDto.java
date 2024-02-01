package com.example.accommodationbooking.dto.user;

public record UserLoginRequestDto(
        String email,
        String password
) {
}

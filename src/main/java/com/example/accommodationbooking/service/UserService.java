package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto registration(UserRegistrationDto userRequestDto);
}

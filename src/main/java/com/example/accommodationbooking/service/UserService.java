package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;

public interface UserService {
    UserResponseDto registration(UserRegistrationDto userRequestDto);

    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto);

    UserResponseDto getUserInformation();

    UserResponseDto addRole(Long id);
}

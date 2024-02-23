package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponseDto registration(UserRegistrationDto userRequestDto);

    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto, Authentication authentication);

    UserResponseDto getUserInformation(Authentication authentication);

    UserResponseDto addRole(Long id,Authentication authentication);
}

package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.user.UserLoginRequestDto;
import com.example.accommodationbooking.dto.user.UserLoginResponseDto;
import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.security.AuthenticationService;
import com.example.accommodationbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto authentication(
            @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }

    @PostMapping("/regist")
    public UserResponseDto registration(
            @RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.registration(userRegistrationDto);
    }
}

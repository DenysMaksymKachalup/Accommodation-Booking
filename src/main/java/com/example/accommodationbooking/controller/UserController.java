package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;
import com.example.accommodationbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDto getUserInformation() {
        return userService.getUserInformation();
    }

    @PutMapping("/me")
    public UserResponseDto update(UserUpdateRequestDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }

    @PutMapping("/{id}/role")
    public UserResponseDto update(@PathVariable Long id) {
        return userService.addRole(id);
    }
}

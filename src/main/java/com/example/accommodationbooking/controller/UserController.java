package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;
import com.example.accommodationbooking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "Endpoints for managing users")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get user information",
            description = "Get information about the current user")
    public UserResponseDto getUserInformation(Authentication authentication) {
        return userService.getUserInformation(authentication);
    }

    @PutMapping("/me")
    @Operation(summary = "Update user information",
            description = "Update information of the current user")
    public UserResponseDto update(@RequestBody UserUpdateRequestDto userUpdateDto,
                                  Authentication authentication) {
        return userService.update(userUpdateDto,authentication);
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role by ID", description = "Update user role by ID")
    public UserResponseDto update(@PathVariable Long id,Authentication authentication) {
        return userService.addRole(id,authentication);
    }
}

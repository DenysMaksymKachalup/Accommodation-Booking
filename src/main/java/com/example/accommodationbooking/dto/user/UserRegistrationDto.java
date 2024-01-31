package com.example.accommodationbooking.dto.user;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record UserRegistrationDto(
        @Email @Length(max = 50) String email,
        @Length(max = 20) String firstName,
        @Length(max = 20) String lastName,
        @Length(min = 3, max = 20) String password,
        @Length(min = 3, max = 20) String repeatPassword
) {
}

package com.example.accommodationbooking.dto.user;

import org.hibernate.validator.constraints.Length;

public record UserUpdateRequestDto(
        @Length(max = 20) String firstName,
        @Length(max = 20) String lastName) {

}

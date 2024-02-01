package com.example.accommodationbooking.dto.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingRequestDto(
        @NotNull LocalDate checkInDate,
        @NotNull LocalDate checkOutDate,
        @NotNull @Positive Long accommodationId,
        @NotNull @Positive Long userId,
        @NotNull String bookingStatus
) {
}

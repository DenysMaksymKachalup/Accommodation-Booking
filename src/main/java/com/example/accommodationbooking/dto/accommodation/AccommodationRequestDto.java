package com.example.accommodationbooking.dto.accommodation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(
        @NotNull String type,
        @NotNull AddressDto address,
        @NotNull String size,
        @NotNull List<String> amenities,
        @Min(1) BigDecimal dailyRate,
        @Min(0) Integer availability) {
}

package com.example.accommodationbooking.dto.accommodation;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationResponseDto(
        Long id,
        String type,
        AddressDto address,
        String size,
        List<String> amenities,
        BigDecimal dailyRate,
        Integer availability
) {
}

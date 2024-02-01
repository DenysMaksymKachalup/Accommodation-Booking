package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import java.util.List;

public interface AccommodationService {
    AccommodationResponseDto save(AccommodationRequestDto accommodationRequestDto);

    AccommodationResponseDto findById(Long id);

    List<AccommodationResponseDto> findAll();

    AccommodationResponseDto updateById(Long id, AccommodationRequestDto accommodationRequestDto);

    void deleteById(Long id);
}

package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.AccommodationMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.repository.AccommodationRepository;
import com.example.accommodationbooking.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public AccommodationResponseDto save(AccommodationRequestDto accommodationRequestDto) {
        Accommodation save = accommodationRepository
                .save(accommodationMapper.toModel(accommodationRequestDto));
        return accommodationMapper.toDto(save);
    }

    @Override
    public AccommodationResponseDto findById(Long id) {
        Accommodation accommodation =
                accommodationRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Accommodation with id: " + id + " not found!"));
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public List<AccommodationResponseDto> findAll() {
        return accommodationRepository.findAll().stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationResponseDto updateById(
            Long id,
            AccommodationRequestDto accommodationRequestDto) {
        findById(id);
        Accommodation updateAccommodation = accommodationMapper
                .toModel(accommodationRequestDto);
        updateAccommodation.setId(id);
        return accommodationMapper.toDto(accommodationRepository.save(updateAccommodation));
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.deleteById(id);
    }

}

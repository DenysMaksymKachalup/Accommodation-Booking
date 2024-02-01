package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/accommodation")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping()
    public AccommodationResponseDto save(
            @RequestBody AccommodationRequestDto accommodationRequestDto) {
        return accommodationService.save(accommodationRequestDto);
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto findById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @GetMapping()
    public List<AccommodationResponseDto> findAll() {
        return accommodationService.findAll();
    }

    @PutMapping("/{id}")
    public AccommodationResponseDto updateById(
            @PathVariable Long id,
            @RequestBody AccommodationRequestDto accommodationRequestDto) {
        return accommodationService.updateById(id,accommodationRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
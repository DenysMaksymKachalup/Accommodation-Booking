package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.service.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodation", description = "Endpoints for managing accommodations")
@RestController
@RequestMapping(value = "/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save accommodation", description = "")
    public AccommodationResponseDto save(
            @RequestBody AccommodationRequestDto accommodationRequestDto) {
        return accommodationService.save(accommodationRequestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find accommodation by ID", description = "Find accommodation by ID")
    public AccommodationResponseDto findById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @GetMapping("/allAccommodation")
    @Operation(summary = "Find all accommodations", description = "Find all accommodations")
    public List<AccommodationResponseDto> findAll() {
        return accommodationService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update accommodation by ID", description = "Update accommodation by ID")
    public AccommodationResponseDto updateById(
            @PathVariable Long id,
            @RequestBody AccommodationRequestDto accommodationRequestDto) {
        return accommodationService.updateById(id,accommodationRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete accommodation by ID", description = "Delete accommodation by ID")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}

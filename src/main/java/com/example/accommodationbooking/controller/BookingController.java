package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking", description = "Endpoints for managing booking")
@RestController
@RequestMapping(value = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    @Operation(summary = "Create booking", description = "Create a new booking")
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingRequestDto,
                                            Authentication authentication) {
        return bookingService.createBooking(bookingRequestDto, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Find booking by ID", description = "Find booking by ID")
    public BookingResponseDto findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @GetMapping("/my")
    @Operation(summary = "Find user's bookings",
            description = "Find all bookings of the current user")
    public List<BookingResponseDto> findUserBookingAll(Authentication authentication) {
        return bookingService.findUserBookingAll(authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    @Operation(summary = "Find bookings by user ID and status",
            description = "Find all bookings by user ID and status")
    public List<BookingResponseDto> findAllByUserIdAndBookingStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        return bookingService.findAllByUserIdAndBookingStatus(id, status);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update user's booking by ID", description = "Update user's booking by ID")
    public BookingResponseDto updateUserBookingById(
            @PathVariable Long id,
            @RequestBody BookingRequestDto bookingRequestDto,
            Authentication authentication) {
        return bookingService.updateUserBookingById(id, bookingRequestDto, authentication);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete booking by ID", description = "Delete booking by ID")
    public void deleteById(@PathVariable Long id, Authentication authentication) {
        bookingService.deleteById(id, authentication);
    }
}

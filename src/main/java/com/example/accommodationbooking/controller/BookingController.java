package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.service.BookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.createBooking(bookingRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public BookingResponseDto findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @GetMapping("/my")
    public List<BookingResponseDto> findUserBookingAll() {
        return bookingService.findUserBookingAll();
    }

    @GetMapping()
    public List<BookingResponseDto> findAllByUserIdAndBookingStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        return bookingService.findAllByUserIdAndBookingStatus(id, status);
    }

    @PutMapping("{id}")
    public BookingResponseDto updateUserBookingById(
            @PathVariable Long id,
            @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.updateUserBookingById(id, bookingRequestDto);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        bookingService.deleteById(id);
    }
}


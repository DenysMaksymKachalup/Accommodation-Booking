package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);

    BookingResponseDto findById(Long id);

    List<BookingResponseDto> findAll();

    List<BookingResponseDto> findUserBookingAll();

    BookingResponseDto updateUserBookingById(Long id, BookingRequestDto bookingRequestDto);

    List<BookingResponseDto> findAllByUserIdAndBookingStatus(Long id, String status);

    void deleteById(Long id);
}

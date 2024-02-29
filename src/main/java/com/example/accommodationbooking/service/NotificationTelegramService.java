package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.model.Booking;

public interface NotificationTelegramService {
    void sendSuccessPaymentText(PaymentResponseWithoutUrlDto paymentResponseWithoutUrlDto);

    void sendCanceledPaymentText(String message);

    void sendSuccessBookingText(BookingResponseDto bookingResponseDto);

    void sendCanceledBookingText(Booking booking);

    void sendCreateAccommodationText(AccommodationResponseDto dto);

    void sendDeletedAccommodationText(Long id);

    void sendNotExpiredBookingText();
}

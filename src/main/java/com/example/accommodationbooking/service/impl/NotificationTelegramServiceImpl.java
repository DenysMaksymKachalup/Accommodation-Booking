package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.service.NotificationTelegramService;
import com.example.accommodationbooking.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTelegramServiceImpl implements NotificationTelegramService {
    private final TelegramBotService telegramBotService;

    @Override
    public void sendSuccessPaymentText(PaymentResponseWithoutUrlDto paymentResponseWithoutUrlDto) {
        String message = String.format("""
                        Payment success! Details:
                        Booking ID: %d
                        Session ID: %s
                        Amount to Pay: %s
                        Payment Status: %s""",
                paymentResponseWithoutUrlDto.bookingId(),
                paymentResponseWithoutUrlDto.sessionId(),
                paymentResponseWithoutUrlDto.amountToPay(),
                paymentResponseWithoutUrlDto.paymentStatus());
        telegramBotService.sendMessage(message);
    }

    @Override
    public void sendCanceledPaymentText(String message) {
        telegramBotService.sendMessage(message);
    }

    @Override
    public void sendNotExpiredBookingText() {
        telegramBotService.sendMessage("No expired bookings today!");
    }

    @Override
    public void sendSuccessBookingText(BookingResponseDto bookingResponseDto) {
        String message = String.format("""
                        Booking success! Your booking details:
                        Booking ID: %d
                        Check-in Date: %s
                        Check-out Date: %s
                        Accommodation ID: %d
                        User ID: %d
                        Booking Status: %s""",
                bookingResponseDto.id(),
                bookingResponseDto.checkInDate(),
                bookingResponseDto.checkOutDate(),
                bookingResponseDto.accommodationId(),
                bookingResponseDto.userId(),
                bookingResponseDto.bookingStatus());
        telegramBotService.sendMessage(message);
    }

    @Override
    public void sendCanceledBookingText(Booking booking) {
        String message = String.format("""
                        Booking with ID %d has been canceled.
                        Check-in Date: %s
                        Check-out Date: %s
                        Accommodation ID: %d
                        User ID: %d""",
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getAccommodation().getId(),
                booking.getUser().getId());
        telegramBotService.sendMessage(message);
    }

    @Override
    public void sendCreateAccommodationText(AccommodationResponseDto dto) {
        String message = String.format("""
                        Accommodation created successfully!
                        Accommodation ID: %d
                        Type: %s
                        Address: %s
                        Size: %s
                        Amenities: %s
                        Daily Rate: %s
                        Availability: %d""",
                dto.id(),
                dto.type(),
                dto.address(),
                dto.size(),
                dto.amenities(),
                dto.dailyRate(),
                dto.availability());
        telegramBotService.sendMessage(message);
    }

    @Override
    public void sendDeletedAccommodationText(Long id) {
        telegramBotService.sendMessage(
                String.format("Accommodation with ID %d has been deleted.", id));
    }
}

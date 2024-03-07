package com.example.accommodationbooking.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.service.impl.NotificationTelegramServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationTelegramServiceImplTest {
    private static final Long BOOKING_ID = 1L;
    private static final Long ACCOMMODATION_ID = 1L;
    private static final Long USER_ID = 1L;
    @Mock
    private TelegramBotService telegramBotService;
    @InjectMocks
    private NotificationTelegramServiceImpl notificationTelegramService;

    @Test
    @DisplayName("Sending success payment text should notify Telegram")
    void send_SuccessPaymentText() {
        PaymentResponseWithoutUrlDto paymentResponse =
                new PaymentResponseWithoutUrlDto(
                        "SUCCESS",
                        1L,
                        "100",
                        BigDecimal.ONE);
        notificationTelegramService.sendSuccessPaymentText(paymentResponse);
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending canceled payment text")
    void send_CanceledPaymentText() {
        notificationTelegramService.sendCanceledPaymentText("Payment canceled");
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending not expired booking text")
    void send_NotExpiredBookingText() {
        notificationTelegramService.sendNotExpiredBookingText();
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending success booking text")
    void send_SuccessBookingText() {
        notificationTelegramService.sendSuccessBookingText(getBookingResponseDto());
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending canceled booking text")
    void send_CanceledBookingText() {
        notificationTelegramService.sendCanceledBookingText(getBooking());
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending create accommodation text")
    void send_CreateAccommodationText() {
        notificationTelegramService.sendCreateAccommodationText(getAccommodationResponseDto());
        verify(telegramBotService).sendMessage(anyString());
    }

    @Test
    @DisplayName("Sending deleted accommodation text")
    void send_DeletedAccommodationText() {
        notificationTelegramService.sendDeletedAccommodationText(1L);
        verify(telegramBotService).sendMessage(anyString());
    }

    private Booking getBooking() {
        Booking booking = new Booking(BOOKING_ID);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setAccommodation(new Accommodation(ACCOMMODATION_ID));
        booking.setUser(new User(USER_ID));
        booking.setCheckInDate(LocalDate.of(2024, 2, 1));
        booking.setCheckOutDate(LocalDate.of(2024, 2, 11));
        return booking;
    }

    private BookingResponseDto getBookingResponseDto() {
        return new BookingResponseDto(
                BOOKING_ID,
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 11),
                ACCOMMODATION_ID,
                USER_ID,
                "PENDING");
    }

    private AccommodationResponseDto getAccommodationResponseDto() {
        AddressDto addressDto = new AddressDto("street", "city");
        return new AccommodationResponseDto(
                ACCOMMODATION_ID,
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );
    }
}

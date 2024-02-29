package com.example.accommodationbooking.service;

import static org.mockito.Mockito.never;

import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.repository.PaymentRepository;
import com.example.accommodationbooking.service.impl.SchedulingServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {

    private static final Long BOOKING_ID = 1L;
    private static final Long ACCOMMODATION_ID = 1L;
    private static final Long USER_ID = 1L;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private NotificationTelegramService notificationTelegramService;

    @InjectMocks
    private SchedulingServiceImpl schedulingService;

    @Test
    @DisplayName("Check Expired Bookings: Not Expired")
    void checkExpiredBookings_NotExpired() {
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(bookingRepository.findAllByBookingStatusAndCheckOutDateIsLessThanEqual(
                        BookingStatus.CONFIRMED, LocalDate.now()))
                .thenReturn(bookings);

        schedulingService.checkExpiredBookings();

        Mockito.verify(notificationTelegramService).sendNotExpiredBookingText();
        Mockito.verify(bookingRepository, never()).saveAll(bookings);
    }

    @Test
    @DisplayName("Check Expired Bookings: Expired")
    void checkExpiredBookings_Expired() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setCheckOutDate(LocalDate.now().minusDays(1));
        List<Booking> bookings = List.of(booking);
        Mockito.when(bookingRepository.findAllByBookingStatusAndCheckOutDateIsLessThanEqual(
                        BookingStatus.CONFIRMED, LocalDate.now()))
                .thenReturn(bookings);

        schedulingService.checkExpiredBookings();

        Mockito.verify(bookingRepository).saveAll(bookings);
        Mockito.verify(notificationTelegramService, never()).sendNotExpiredBookingText();
    }

    @Test
    @DisplayName("Check Payment Creation")
    void checkPaymentCreation() {
        Booking booking = getBooking();
        List<Booking> bookings = List.of(booking);

        Booking bookingUpdateStatus = getBooking();
        bookingUpdateStatus.setBookingStatus(BookingStatus.CONFIRMED);

        Mockito.when(bookingRepository.findBookingByBookingStatus(BookingStatus.PENDING))
                .thenReturn(bookings);
        Mockito.when(paymentRepository.existsById(booking.getId())).thenReturn(false);

        schedulingService.checkPaymentCreation();

        Mockito.verify(bookingRepository).saveAll(bookings);
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
}

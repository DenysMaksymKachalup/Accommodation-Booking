package com.example.accommodationbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.exception.PaymentException;
import com.example.accommodationbooking.mapper.PaymentMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.Payment;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.model.enumeration.PaymentStatus;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.repository.PaymentRepository;
import com.example.accommodationbooking.service.impl.PaymentServiceImpl;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceImplTest {

    private static final Long BOOKING_ID = 1L;
    private static final Long ACCOMMODATION_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long PAYMENT_ID = 1L;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private NotificationTelegramService notificationTelegramService;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private URL url;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Find All Payments by User ID")
    public void findAll_byUserId_returnPaymentResponseDtoList() {
        Long userId = 1L;

        Payment payment = getPayment();

        PaymentResponseDto paymentResponseDto
                = new PaymentResponseDto("PAID", BOOKING_ID, url, "sessionID", BigDecimal.ONE);

        when(paymentRepository.findAllByBookingUserId(userId)).thenReturn(List.of(payment));
        when(paymentMapper.toDto(any()))
                .thenReturn(paymentResponseDto);

        // Act
        List<PaymentResponseDto> actualPaymentDtos = paymentService.findAllByUserId(userId);

        // Assert
        assertEquals(paymentResponseDto, actualPaymentDtos.getFirst());

    }

    @Test
    @DisplayName("Handle Successful Payment with Valid Session "
            + "ID Should Not Update Booking and Payment Status")
    public void handleSuccessfulPayment_ValidSessionId_NotUpdateBookingAndPaymentStatus() {
        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.EXPIRED);
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentStatus(PaymentStatus.PAID);
        String sessionId = "sessionId";

        PaymentResponseWithoutUrlDto paymentResponseWithoutUrlDto =
                new PaymentResponseWithoutUrlDto("PENDING", BOOKING_ID, sessionId, BigDecimal.ONE);

        when(paymentMapper.toDtoWithoutUrl(payment)).thenReturn(paymentResponseWithoutUrlDto);
        when(paymentRepository.findBySessionId(anyString())).thenReturn(Optional.of(payment));
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        paymentService.handleSuccessfulPayment(sessionId);

        assertEquals(BookingStatus.EXPIRED, booking.getBookingStatus());
        assertEquals(PaymentStatus.PAID, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("Handle Successful Payment with Invalid Session ID Should Throw PaymentException")
    void handleSuccessfulPayment_InvalidSessionId_ThrowsPaymentException() {
        String invalidSessionId = "invalidSessionId";

        when(paymentRepository.findBySessionId(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(PaymentException.class, () ->
                paymentService.handleSuccessfulPayment(invalidSessionId));
    }

    private Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(PAYMENT_ID);
        payment.setSessionId("sessionID");
        payment.setAmountToPay(BigDecimal.ONE);
        payment.setSessionUrl("URL");
        payment.setBooking(getBooking());
        payment.setPaymentStatus(PaymentStatus.PAID);
        return payment;
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

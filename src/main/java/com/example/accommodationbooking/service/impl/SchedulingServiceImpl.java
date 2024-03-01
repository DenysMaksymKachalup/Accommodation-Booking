package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.exception.PaymentException;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.Payment;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.model.enumeration.PaymentStatus;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.repository.PaymentRepository;
import com.example.accommodationbooking.service.BookingService;
import com.example.accommodationbooking.service.NotificationTelegramService;
import com.example.accommodationbooking.service.SchedulingService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationTelegramService notificationTelegramService;

    @Override
    @Scheduled(cron = "0 0 15 * * *")
    public void checkExpiredBookings() {
        List<Booking> list = bookingRepository
                .findAllByBookingStatusAndCheckOutDateIsLessThanEqual(
                        BookingStatus.CONFIRMED, LocalDate.now())
                .stream()
                .map(booking -> updateBookingStatus(booking, BookingStatus.EXPIRED))
                .toList();

        if (list.isEmpty()) {
            notificationTelegramService.sendNotExpiredBookingText();
        } else {
            bookingRepository.saveAll(list);
        }
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void checkExpiredPayments() {
        List<Payment> list = paymentRepository.findAllByPaymentStatus(PaymentStatus.PENDING)
                .stream()
                .map(this::updatePaymentStatus)
                .toList();
        paymentRepository.saveAll(list);
    }

    @Scheduled(fixedDelay = 20000)
    @Override
    public void checkPaymentCreation() {
        List<Booking> list = bookingRepository.findBookingByBookingStatus(BookingStatus.PENDING)
                .stream()
                .filter(booking -> !paymentRepository.existsById(booking.getId()))
                .toList();
        list.forEach(booking -> updateBookingStatus(booking, BookingStatus.CANCELED));
        bookingRepository.saveAll(list);
    }

    private Booking updateBookingStatus(Booking booking, BookingStatus status) {
        booking.setBookingStatus(status);
        return booking;
    }

    private Payment updatePaymentStatus(Payment payment) {

        try {
            String sessionId = payment.getSessionId();
            Session session = Session.retrieve(sessionId);
            Long expiresAt = session.getExpiresAt();
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(expiresAt),
                    ZoneId.systemDefault());

            if (dateTime.isBefore(LocalDateTime.now())) {
                bookingService.updateStatus(payment.getBooking().getId(), BookingStatus.CANCELED);
                payment.setPaymentStatus(PaymentStatus.EXPIRED);
            }

            return payment;
        } catch (StripeException e) {
            throw new PaymentException("Cant retrieve the session!");
        }
    }
}

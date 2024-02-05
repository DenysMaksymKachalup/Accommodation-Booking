package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.exception.PaymentException;
import com.example.accommodationbooking.mapper.PaymentMapper;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.Payment;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumaration.BookingStatus;
import com.example.accommodationbooking.model.enumaration.PaymentStatus;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.repository.PaymentRepository;
import com.example.accommodationbooking.service.BookingService;
import com.example.accommodationbooking.service.NotificationTelegramService;
import com.example.accommodationbooking.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Long DEFAULT_QUANTITY = 1L;
    private static final String PRODUCT_DATA_NAME = "Payment for booking";
    private static final String PAYMENT_CURRENCY_USD = "usd";
    private static final BigDecimal PRICE_CORRECTION = BigDecimal.valueOf(100L);
    private static final String CANCEL_URL = "http://localhost:8080/payments"
            + "/cancel?session_id={CHECKOUT_SESSION_ID}";
    private static final String SUCCESS_URL = "http://localhost:8080/payments"
            + "/success?session_id={CHECKOUT_SESSION_ID}";

    private final BookingService bookingService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BookingRepository bookingRepository;
    private final NotificationTelegramService notificationTelegramService;

    @Value("${stripe.secret.key}")
    private String stripe;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripe;
    }

    @Transactional
    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        Session session;
        try {
            session = Session.create(getSessionCreateParams(paymentRequestDto));
            return paymentMapper.toDto(savePayment(session, paymentRequestDto));
        } catch (StripeException e) {
            throw new PaymentException("Cant pay for booking!", e);
        }
    }

    @Override
    public List<PaymentResponseDto> findAllByUserId(Long id) {
        return paymentRepository.findAllByBookingUserId(id).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentResponseCancelDto handleCanceledPayment(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            LocalDate localDate = LocalDate.ofEpochDay(session.getExpiresAt());
            String message = "Ooops, something happened. You can try again until: " + localDate;
            notificationTelegramService.sendCanceledPaymentText(message);
            return new PaymentResponseCancelDto(message);
        } catch (StripeException e) {
            throw new PaymentException("Cant find session: " + sessionId, e);
        }
    }

    @Override
    public PaymentResponseWithoutUrlDto handleSuccessfulPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() ->
                        new PaymentException("Payment with sessionId: " + sessionId
                                + " not found!"));
        Long id = payment.getBooking().getId();
        Booking booking = findBookingById(id);
        if (payment.getPaymentStatus().equals(PaymentStatus.PENDING)
                && booking.getBookingStatus().equals(BookingStatus.PENDING)) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            payment.setPaymentStatus(PaymentStatus.PAID);
            bookingRepository.save(booking);
            paymentRepository.save(payment);
        }
        PaymentResponseWithoutUrlDto dtoWithoutUrl = paymentMapper.toDtoWithoutUrl(payment);
        notificationTelegramService.sendSuccessPaymentText(dtoWithoutUrl);
        return dtoWithoutUrl;
    }

    private SessionCreateParams getSessionCreateParams(PaymentRequestDto paymentRequestDto) {
        return SessionCreateParams.builder()
                .setCustomerEmail(getUser().getEmail())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(DEFAULT_QUANTITY)
                                .setPriceData(getPriceData(paymentRequestDto))
                                .build())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData getPriceData(
            PaymentRequestDto paymentRequestDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(PAYMENT_CURRENCY_USD)
                .setUnitAmountDecimal(getTotalPrice(paymentRequestDto))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(PRODUCT_DATA_NAME)
                                .setDescription(getBookingDescription(paymentRequestDto))
                                .build()
                )
                .build();
    }

    private BigDecimal getTotalPrice(PaymentRequestDto paymentRequestDto) {
        Booking booking = findBookingById(paymentRequestDto.bookingId());
        long daysBetween = ChronoUnit.DAYS.between(
                booking.getCheckInDate(),
                booking.getCheckOutDate());

        return booking.getAccommodation().getDailyRate()
                .multiply(BigDecimal.valueOf(daysBetween))
                .multiply(PRICE_CORRECTION);
    }

    public String getBookingDescription(PaymentRequestDto payment) {
        Booking booking = findBookingById(payment.bookingId());
        return "Payment booking for "
                + booking.getAccommodation().getType()
                + " at address: "
                + booking.getAccommodation().getAddress().getCity()
                + " "
                + booking.getAccommodation().getAddress().getStreet();
    }

    private Booking findBookingById(Long id) {
        bookingService.findUserBookingAll().stream()
                .filter(booking -> booking.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Booking with id: " + id + "not found!"));
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Booking with id: " + id + "not found!"));
    }

    private Payment savePayment(Session session, PaymentRequestDto paymentRequestDto) {
        Payment payment = new Payment();
        payment.setSessionUrl(session.getUrl());
        payment.setBooking(findBookingById(paymentRequestDto.bookingId()));
        payment.setSessionId(session.getId());
        payment.setAmountToPay(BigDecimal.valueOf(session.getAmountSubtotal())
                .divide(PRICE_CORRECTION, RoundingMode.valueOf(3)));
        return paymentRepository.save(payment);
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

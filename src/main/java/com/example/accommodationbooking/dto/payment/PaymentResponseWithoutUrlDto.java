package com.example.accommodationbooking.dto.payment;

import java.math.BigDecimal;
import java.net.URL;

public record PaymentResponseWithoutUrlDto(
        String paymentStatus,
        Long bookingId,
        String sessionId,
        BigDecimal amountToPay
) {
}

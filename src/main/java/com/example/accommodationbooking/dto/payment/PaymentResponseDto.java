package com.example.accommodationbooking.dto.payment;

import java.math.BigDecimal;
import java.net.URL;

public record PaymentResponseDto(
        String paymentStatus,
        Long bookingId,
        URL sessionUrl,
        String sessionId,
        BigDecimal amountToPay
) {
}

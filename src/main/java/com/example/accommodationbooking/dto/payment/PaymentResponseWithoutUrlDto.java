package com.example.accommodationbooking.dto.payment;

import java.math.BigDecimal;

public record PaymentResponseWithoutUrlDto(
        String paymentStatus,
        Long bookingId,
        String sessionId,
        BigDecimal amountToPay
) {
}

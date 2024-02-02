package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);

    List<PaymentResponseDto> findAllByUserId(Long id);

    PaymentResponseCancelDto handleCanceledPayment(String sessionId);

    PaymentResponseWithoutUrlDto handleSuccessfulPayment(String sessionId);
}

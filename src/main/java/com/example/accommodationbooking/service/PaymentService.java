package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto, Authentication authentication);

    List<PaymentResponseDto> findAllByUserId(Long id);

    PaymentResponseCancelDto handleCanceledPayment(String sessionId);

    PaymentResponseWithoutUrlDto handleSuccessfulPayment(String sessionId);
}

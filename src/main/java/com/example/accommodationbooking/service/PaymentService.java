package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto,
                                     Authentication authentication);

    List<PaymentResponseDto> findAllByUserId(Long id);

    PaymentResponseCancelDto handleCanceledPayment(String sessionId);

    PaymentResponseWithoutUrlDto handleSuccessfulPayment(String sessionId);
}

package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto createPaymentSession(
            @RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return paymentService.createPayment(paymentRequestDto);
    }

    @GetMapping("/success")
    public PaymentResponseWithoutUrlDto handleSuccessfulPayment(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handleSuccessfulPayment(sessionId);
    }

    @GetMapping("/cancel")
    public PaymentResponseCancelDto handleCanceledPayment(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handleCanceledPayment(sessionId);
    }

    @GetMapping()
    public List<PaymentResponseDto> findAllByUserId(
            @RequestParam("user_id") Long id) {
        return paymentService.findAllByUserId(id);
    }
}

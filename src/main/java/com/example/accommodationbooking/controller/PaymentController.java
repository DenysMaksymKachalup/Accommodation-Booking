package com.example.accommodationbooking.controller;

import com.example.accommodationbooking.dto.payment.PaymentRequestDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseCancelDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseDto;
import com.example.accommodationbooking.dto.payment.PaymentResponseWithoutUrlDto;
import com.example.accommodationbooking.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payments", description = "Endpoints for managing payments")
@RestController
@RequestMapping(value = "payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create payment session", description = "Create a new payment session")
    public PaymentResponseDto createPaymentSession(
            @RequestBody @Valid PaymentRequestDto paymentRequestDto,
            Authentication authentication) {
        return paymentService.createPayment(paymentRequestDto,authentication);
    }

    @GetMapping("/success")
    @Operation(summary = "Handle successful payment", description = "Handle successful payment")
    public PaymentResponseWithoutUrlDto handleSuccessfulPayment(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handleSuccessfulPayment(sessionId);
    }

    @GetMapping("/cancel")
    @Operation(summary = "Handle canceled payment", description = "Handle canceled payment")
    public PaymentResponseCancelDto handleCanceledPayment(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handleCanceledPayment(sessionId);
    }

    @GetMapping()
    @Operation(summary = "Find all payments by user ID",
            description = "Find all payments by user ID")
    public List<PaymentResponseDto> findAllByUserId(
            @RequestParam("user_id") Long id) {
        return paymentService.findAllByUserId(id);
    }
}

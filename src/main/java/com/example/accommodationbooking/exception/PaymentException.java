package com.example.accommodationbooking.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}

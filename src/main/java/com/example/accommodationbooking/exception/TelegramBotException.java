package com.example.accommodationbooking.exception;

public class TelegramBotException extends RuntimeException {
    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }
}

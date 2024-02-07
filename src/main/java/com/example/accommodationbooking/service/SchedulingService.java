package com.example.accommodationbooking.service;

public interface SchedulingService {
    void checkExpiredBookings();

    void checkExpiredPayments();

    void checkPaymentCreation();
}

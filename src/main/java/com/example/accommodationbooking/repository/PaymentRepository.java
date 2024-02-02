package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findAllByBookingUserId(Long id);
    Optional<Payment> findBySessionId(String sessionId);
}

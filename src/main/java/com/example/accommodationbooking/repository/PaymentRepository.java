package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Payment;
import com.example.accommodationbooking.model.enumeration.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBookingUserId(Long id);

    Optional<Payment> findBySessionId(String sessionId);

    List<Payment> findAllByPaymentStatus(PaymentStatus status);
}

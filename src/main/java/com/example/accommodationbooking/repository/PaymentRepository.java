package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}

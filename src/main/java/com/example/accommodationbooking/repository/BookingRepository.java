package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.enumaration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findAllByUserId(Long id);

    List<Booking> findAllByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);
}

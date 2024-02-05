package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findAllByUserId(Long id);

    List<Booking> findAllByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);

    List<Booking> findAllByCheckInDateBetween(
            LocalDate checkInDate, LocalDate checkOutDate);

    @Query("SELECT b FROM Booking b WHERE (:checkInDate BETWEEN b.checkInDate AND b.checkOutDate) "
            + "OR (:checkOutDate BETWEEN b.checkInDate AND b.checkOutDate)"
            + "OR (b.checkInDate BETWEEN :checkInDate AND :checkOutDate)"
            + "OR (b.checkOutDate BETWEEN :checkInDate AND :checkOutDate)"
            + "AND (b.accommodation.id = :id)")
    List<Booking> findAllByCheckOutDateBetween(
            Long id, LocalDate checkInDate, LocalDate checkOutDate);

}

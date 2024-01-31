package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
}

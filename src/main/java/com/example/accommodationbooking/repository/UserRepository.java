package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findUserByEmail(String email);
}

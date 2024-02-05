package com.example.accommodationbooking.repository;

import com.example.accommodationbooking.model.Role;
import com.example.accommodationbooking.model.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName role);
}

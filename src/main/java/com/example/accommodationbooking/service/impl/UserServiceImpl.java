package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.mapper.UserMapper;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.RoleName;
import com.example.accommodationbooking.repository.RoleRepository;
import com.example.accommodationbooking.repository.UserRepository;
import com.example.accommodationbooking.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registration(UserRegistrationDto userRequestDto) {
        if (userRepository.existsByEmailIgnoreCase(userRequestDto.email())) {
            throw new RuntimeException(
                    "User with email: " + userRequestDto.email() + " exist");
        }
        User user = userMapper.toModel(userRequestDto);
        user.setRoles(Set.of(roleRepository.findByName(RoleName.ROLE_USER)));
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        return userMapper.toDto(userRepository.save(user));
    }
}

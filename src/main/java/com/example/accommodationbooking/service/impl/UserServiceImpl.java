package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.UserMapper;
import com.example.accommodationbooking.model.Role;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.RoleName;
import com.example.accommodationbooking.repository.RoleRepository;
import com.example.accommodationbooking.repository.UserRepository;
import com.example.accommodationbooking.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @Override
    public UserResponseDto update(UserUpdateRequestDto userUpdateDto,
                                  Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        user.setFirstName(userUpdateDto.firstName());
        user.setLastName(userUpdateDto.lastName());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserInformation(Authentication authentication) {
        return userMapper.toDto(getUserFromAuthentication(authentication));
    }

    @Override
    public UserResponseDto addRole(Long id, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        Set<Role> roles = new HashSet<>();
        roles.add(getRoleById(id));
        roles.addAll(user.getRoles());
        user.setRoles(roles);
        return userMapper.toDto(userRepository.save(user));
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String name = authentication.getName();
        return userRepository.findUserByEmail(name)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    private Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cant find role with id: " + id));
    }
}

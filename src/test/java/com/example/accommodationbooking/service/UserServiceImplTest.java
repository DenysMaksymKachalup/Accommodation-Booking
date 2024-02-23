package com.example.accommodationbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.dto.user.UserUpdateRequestDto;
import com.example.accommodationbooking.mapper.UserMapper;
import com.example.accommodationbooking.model.Role;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.RoleName;
import com.example.accommodationbooking.repository.RoleRepository;
import com.example.accommodationbooking.repository.UserRepository;
import com.example.accommodationbooking.service.impl.UserServiceImpl;
import java.util.Optional;
import java.util.Set;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final User user = new User(USER_ID);

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeAll
    @SneakyThrows
    static void setup(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) {

    }

    @Test
    @DisplayName("Test user registration")
    @WithMockUser()
    public void registration_ValidDataNewUser_returnUserResponseDto() {
        Role role = new Role();
        role.setId(2L);
        role.setName(RoleName.ROLE_ADMIN);

        UserResponseDto userDto = getUserResponse();
        UserRegistrationDto userRegistrationDto = getUserRegistration();

        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(role);
        Mockito.when(userMapper.toModel(userRegistrationDto)).thenReturn(user);
        Mockito.when(userRepository.existsByEmailIgnoreCase("admin")).thenReturn(false);
        Mockito.when(userMapper.toDto(user)).thenReturn(userDto);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        UserResponseDto responseDto = userService.registration(userRegistrationDto);

        assertNotNull(responseDto);
        assertEquals("email", responseDto.email());
        assertEquals("first", responseDto.firstName());
        assertEquals("last", responseDto.lastName());
    }

    @Test
    @DisplayName("Test user update")
    @WithMockUser()
    public void update_validId_returnUserResponseDto() {
        UserResponseDto userUpdateDto = new UserResponseDto(USER_ID, "email", "firstU", "lastU");
        User userUpdated = new User(USER_ID);
        userUpdated.setLastName("lastU");
        userUpdated.setFirstName("firstU");
        UserUpdateRequestDto userUpdateRequestDto =
                new UserUpdateRequestDto("firstU", "lastU");

        Mockito.when(userMapper.toDto(userUpdated)).thenReturn(userUpdateDto);
        Mockito.when(userRepository.save(any())).thenReturn(userUpdated);

        UserResponseDto responseDto = userService.update(userUpdateRequestDto,authentication);

        assertNotNull(responseDto);
        assertEquals("email", responseDto.email());
        assertEquals("firstU", responseDto.firstName());
        assertEquals("lastU", responseDto.lastName());
    }

    @Test
    @DisplayName("Test get user information")
    @WithUserDetails("admin")
    public void getInformation_returnUserResponseDto() {
        UserResponseDto userDto = getUserResponse();

        Mockito.when(userMapper.toDto(user)).thenReturn(userDto);

        UserResponseDto responseDto = userService.getUserInformation(authentication);

        assertNotNull(responseDto);
        assertEquals("email", responseDto.email());
        assertEquals("first", responseDto.firstName());
        assertEquals("last", responseDto.lastName());
    }

    @Test
    @DisplayName("Test add role")
    @WithUserDetails("admin")
    public void addRole_validRoleId_returnUserResponseDto() {
        User userWithRole = new User(USER_ID);
        userWithRole.setRoles(Set.of(new Role(1L, RoleName.ROLE_USER),
                new Role(2L, RoleName.ROLE_ADMIN)));

        Mockito.when(userRepository.save(any())).thenReturn(userWithRole);
        Mockito.when(roleRepository.findById(1L))
                .thenReturn(Optional.of(new Role(2L, RoleName.ROLE_ADMIN)));
        Mockito.when(userMapper.toDto(userWithRole)).thenReturn(getUserResponse());

        UserResponseDto responseDto = userService.addRole(1L,authentication);

        assertNotNull(responseDto);
        assertEquals("email", responseDto.email());
    }

    private UserRegistrationDto getUserRegistration() {
        return new UserRegistrationDto(
                "email",
                "first",
                "last",
                "password",
                "password");
    }

    private UserResponseDto getUserResponse() {
        return new UserResponseDto(USER_ID,
                "email", "first", "last");
    }
}

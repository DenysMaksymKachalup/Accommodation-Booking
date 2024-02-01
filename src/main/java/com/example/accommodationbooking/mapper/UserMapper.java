package com.example.accommodationbooking.mapper;

import com.example.accommodationbooking.config.MapperConfig;
import com.example.accommodationbooking.dto.user.UserRegistrationDto;
import com.example.accommodationbooking.dto.user.UserResponseDto;
import com.example.accommodationbooking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationDto userRegistrationDto);

    UserResponseDto toDto(User user);

    @Named("userById")
    default User userById(Long userId) {
        return new User(userId);
    }
}

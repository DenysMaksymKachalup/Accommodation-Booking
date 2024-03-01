package com.example.accommodationbooking.mapper;

import com.example.accommodationbooking.config.MapperConfig;
import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class,
        uses = {AccommodationMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(source = "accommodation.id", target = "accommodationId")
    @Mapping(source = "user.id", target = "userId")
    BookingResponseDto toDto(Booking booking);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userById")
    @Mapping(source = "bookingRequestDto.accommodationId",
            target = "accommodation",
            qualifiedByName = "accommodationById")
    Booking toModel(Long userId, BookingRequestDto bookingRequestDto);
}

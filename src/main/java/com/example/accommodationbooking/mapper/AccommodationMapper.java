package com.example.accommodationbooking.mapper;

import com.example.accommodationbooking.config.MapperConfig;
import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface AccommodationMapper {
    AccommodationResponseDto toDto(Accommodation accommodation);

    Accommodation toModel(AccommodationRequestDto accommodationDto);

    @Named("accommodationById")
    default Accommodation accommodationById(Long accommodationId) {
        return new Accommodation(accommodationId);
    }
}

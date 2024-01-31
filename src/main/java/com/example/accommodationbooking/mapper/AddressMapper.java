package com.example.accommodationbooking.mapper;

import com.example.accommodationbooking.config.MapperConfig;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.example.accommodationbooking.model.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address toModel(AddressDto addressDto);
}

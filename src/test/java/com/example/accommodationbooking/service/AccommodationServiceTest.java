package com.example.accommodationbooking.service;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.AccommodationMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Address;
import com.example.accommodationbooking.model.enumeration.TypeBuilding;
import com.example.accommodationbooking.repository.AccommodationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccommodationServiceTest {
    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationMapper accommodationMapper;

    @InjectMocks
    private AccommodationService accommodationService;

    @Test
    @DisplayName("")
    public void save_ValidAccommodationRequestDto_returnAccommodationDto() {
        AddressDto addressDto = new AddressDto("street","city");
        AccommodationRequestDto accommodationRequestDto = new AccommodationRequestDto(
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );

        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Mockito.when(accommodationMapper.toModel(accommodationRequestDto))
                .thenReturn(accommodation);
        Mockito.when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);
        Mockito.when(accommodationRepository.save(accommodation))
                .thenReturn(accommodation);

        AccommodationResponseDto actual = accommodationService.save(accommodationRequestDto);

        assertEquals(accommodationResponseDto,actual);
    }

    @Test
    @DisplayName("")
    public void findAll_returnAccommodationDtoList() {
        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Mockito.when(accommodationRepository.findAll())
                .thenReturn(List.of(accommodation));
        Mockito.when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);

        List<AccommodationResponseDto> actual = accommodationService.findAll();

        assertEquals(List.of(accommodationResponseDto),actual);
    }


    @Test
    @DisplayName("")
    public void find_byCorrectId_returnAccommodationDto() {
        Long accommodationId = 1L;
        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Mockito.when(accommodationRepository.findById(accommodationId))
                .thenReturn(Optional.of(accommodation));
        Mockito.when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);

        AccommodationResponseDto actual = accommodationService.findById(accommodationId);

        assertEquals(accommodationResponseDto,actual);

    }

    @Test
    @DisplayName("")
    public void find_byIncorrectId_throwEntityNotFoundException() {
        Long accommodationId = 10L;
        Mockito.when(accommodationRepository.findById(accommodationId))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> accommodationService.findById(accommodationId));
    }

    @Test
    public void update_byCorrectId_return() {
        AddressDto addressDto = new AddressDto("street","city");
        AccommodationRequestDto accommodationRequestDto = new AccommodationRequestDto(
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );

        Long accommodationId = 1L;
        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        AccommodationResponseDto accommodationUpdateResponseDto =
                getAccommodationUpdateResponseDto();
        Accommodation accommodationForUpdating = getAccommodation();
        accommodationForUpdating.setAvailability(5);

        Mockito.when(accommodationRepository.findById(accommodationId))
                .thenReturn(Optional.of(accommodation));
        Mockito.when(accommodationService.findById(accommodationId))
                .thenReturn(accommodationResponseDto);
        Mockito.when(accommodationMapper.toModel(accommodationRequestDto))
                .thenReturn(accommodation);
        Mockito.when(accommodationMapper.toDto(any()))
                .thenReturn(accommodationUpdateResponseDto);
        Mockito.when(accommodationRepository.save(any(Accommodation.class)))
                .thenReturn(accommodationForUpdating);

        AccommodationResponseDto actual =
                accommodationService.updateById(accommodationId, accommodationRequestDto);

        assertEquals(accommodationUpdateResponseDto,actual);
    }

    @Test
    @DisplayName("")
    public void delete_byIdCorrect_() {
        Long accommodationId = 1L;
        Mockito.doNothing().when(accommodationRepository).deleteById(accommodationId);
        accommodationService.deleteById(accommodationId);
        Mockito.verify(accommodationRepository,
                Mockito.times(1)).deleteById(accommodationId);
    }
    private Accommodation getAccommodation() {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");
        Accommodation accommodation = new Accommodation(1L);
        accommodation.setAvailability(1);
        accommodation.setSize("size");
        accommodation.setType(TypeBuilding.HOUSE);
        accommodation.setAddress(address);
        accommodation.setAmenities(List.of("amenities"));
        accommodation.setDailyRate(BigDecimal.ONE);

        return accommodation;
    }

    private AccommodationResponseDto getAccommodationResponseDto() {
        AddressDto addressDto = new AddressDto("street","city");
        return new AccommodationResponseDto(
                1L,
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );
    }
    private AccommodationResponseDto getAccommodationUpdateResponseDto() {
        AddressDto addressDto = new AddressDto("street","city");
        return new AccommodationResponseDto(
                1L,
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                5
        );
    }
}

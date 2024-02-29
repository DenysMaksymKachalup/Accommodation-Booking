package com.example.accommodationbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.AccommodationMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Address;
import com.example.accommodationbooking.model.enumeration.TypeBuilding;
import com.example.accommodationbooking.repository.AccommodationRepository;
import com.example.accommodationbooking.service.impl.AccommodationServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccommodationServiceTest {
    private static final Long ACCOMMODATION_ID = 1L;
    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationMapper accommodationMapper;

    @Mock
    private NotificationTelegramService notificationTelegramService;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    @DisplayName("Test save method with valid "
            + "AccommodationRequestDto should return AccommodationDto")
    public void save_validAccommodationRequestDto_returnAccommodationDto() {
        AddressDto addressDto = new AddressDto("street", "city");
        AccommodationRequestDto accommodationRequestDto =
                new AccommodationRequestDto(
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
        Mockito.doNothing().when(notificationTelegramService)
                .sendCreateAccommodationText(accommodationResponseDto);

        AccommodationResponseDto actual = accommodationService.save(accommodationRequestDto);

        assertEquals(accommodationResponseDto, actual);
    }

    @Test
    @DisplayName("Test findAll method should return list of AccommodationDto")
    public void findAll_returnAccommodationDtoList() {
        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Mockito.when(accommodationRepository.findAll())
                .thenReturn(List.of(accommodation));
        Mockito.when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);

        List<AccommodationResponseDto> actual = accommodationService.findAll();

        assertEquals(List.of(accommodationResponseDto), actual);
    }

    @Test
    @DisplayName("Test findById method with correct ID should return AccommodationDto")
    public void find_byCorrectId_returnAccommodationDto() {
        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Mockito.when(accommodationRepository.findById(ACCOMMODATION_ID))
                .thenReturn(Optional.of(accommodation));
        Mockito.when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);

        AccommodationResponseDto actual = accommodationService.findById(ACCOMMODATION_ID);

        assertEquals(accommodationResponseDto, actual);

    }

    @Test
    @DisplayName("Test findById method with incorrect ID should throw EntityNotFoundException")
    public void find_byIncorrectId_throwEntityNotFoundException() {
        Mockito.when(accommodationRepository.findById(ACCOMMODATION_ID))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> accommodationService.findById(ACCOMMODATION_ID));
    }

    @Test
    @DisplayName("Test updateById method with correct ID should return updated AccommodationDto")
    public void update_byCorrectId_returnAccommodationDto() {

        Accommodation accommodation = getAccommodation();
        AccommodationResponseDto accommodationResponseDto = getAccommodationResponseDto();

        Accommodation accommodationForUpdating = getAccommodation();
        accommodationForUpdating.setAvailability(5);
        AddressDto addressDto = new AddressDto("street", "city");
        AccommodationRequestDto accommodationRequestDto = new AccommodationRequestDto(
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );
        AccommodationResponseDto accommodationUpdateResponseDto =
                getAccommodationUpdateResponseDto();

        Mockito.when(accommodationRepository.findById(ACCOMMODATION_ID))
                .thenReturn(Optional.of(accommodation));
        Mockito.when(accommodationService.findById(ACCOMMODATION_ID))
                .thenReturn(accommodationResponseDto);
        Mockito.when(accommodationMapper.toModel(accommodationRequestDto))
                .thenReturn(accommodation);
        Mockito.when(accommodationMapper.toDto(any()))
                .thenReturn(accommodationUpdateResponseDto);
        Mockito.when(accommodationRepository.save(any(Accommodation.class)))
                .thenReturn(accommodationForUpdating);

        AccommodationResponseDto actual =
                accommodationService.updateById(ACCOMMODATION_ID, accommodationRequestDto);

        assertEquals(accommodationUpdateResponseDto, actual);
    }

    @Test
    @DisplayName("Test deleteById method with correct ID should delete Accommodation")
    public void delete_byIdCorrect() {
        Mockito.doNothing().when(notificationTelegramService)
                .sendDeletedAccommodationText(ACCOMMODATION_ID);
        Mockito.doNothing().when(accommodationRepository).deleteById(ACCOMMODATION_ID);
        accommodationService.deleteById(ACCOMMODATION_ID);
        Mockito.verify(accommodationRepository,
                Mockito.times(1)).deleteById(ACCOMMODATION_ID);
    }

    private Accommodation getAccommodation() {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");
        Accommodation accommodation = new Accommodation(ACCOMMODATION_ID);
        accommodation.setAvailability(1);
        accommodation.setSize("size");
        accommodation.setType(TypeBuilding.HOUSE);
        accommodation.setAddress(address);
        accommodation.setAmenities(List.of("amenities"));
        accommodation.setDailyRate(BigDecimal.ONE);

        return accommodation;
    }

    private AccommodationResponseDto getAccommodationResponseDto() {
        AddressDto addressDto = new AddressDto("street", "city");
        return new AccommodationResponseDto(
                ACCOMMODATION_ID,
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                1
        );
    }

    private AccommodationResponseDto getAccommodationUpdateResponseDto() {
        AddressDto addressDto = new AddressDto("street", "city");
        return new AccommodationResponseDto(
                ACCOMMODATION_ID,
                "HOUSE",
                addressDto,
                "size",
                List.of("amenities"),
                BigDecimal.ONE,
                5
        );
    }
}

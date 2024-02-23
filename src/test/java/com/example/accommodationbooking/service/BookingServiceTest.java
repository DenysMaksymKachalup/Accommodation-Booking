package com.example.accommodationbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.accommodationbooking.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.dto.booking.BookingResponseDto;
import com.example.accommodationbooking.exception.BookingException;
import com.example.accommodationbooking.exception.EntityNotFoundException;
import com.example.accommodationbooking.mapper.BookingMapper;
import com.example.accommodationbooking.model.Accommodation;
import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.Role;
import com.example.accommodationbooking.model.User;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.example.accommodationbooking.model.enumeration.RoleName;
import com.example.accommodationbooking.repository.BookingRepository;
import com.example.accommodationbooking.service.impl.BookingServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    private static final Long BOOKING_ID = 1L;
    private static final Long ACCOMMODATION_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final User user = new User(USER_ID);

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private NotificationTelegramService notificationTelegramService;

    @Mock
    private AccommodationService accommodationService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    Authentication authentication;

    @BeforeAll
    static void setUp() {
        user.setFirstName("firstName");
        user.setLastName("LastName");
        user.setEmail("admin");
        user.setPassword("password");
        user.setRoles(Set.of(new Role(2L, RoleName.ROLE_ADMIN)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Test createBooking method with valid "
            + "BookingRequestDto should return BookingResponseDto")
    @WithMockUser(username = "admin",roles = "ADMIN")
    public void save_validBookingRequestDto_returnBookingDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 11),
                1L,
                "PENDING"
        );

        Booking booking = getBooking();
        BookingResponseDto bookingResponse = getBookingResponseDto();

        Mockito.when(accommodationService.findById(ACCOMMODATION_ID))
                .thenReturn(getAccommodationResponseDto());
        Mockito.when(bookingMapper.toModel(USER_ID, bookingRequestDto)).thenReturn(booking);
        Mockito.when(bookingRepository.save(booking)).thenReturn(booking);
        Mockito.when(bookingMapper.toDto(booking)).thenReturn(bookingResponse);
        Mockito.doNothing().when(notificationTelegramService)
                .sendSuccessBookingText(bookingResponse);

        BookingResponseDto actual = bookingService.createBooking(bookingRequestDto,authentication);

        assertEquals(bookingResponse, actual);
    }

    @Test
    @DisplayName("Test findById method with correct ID should return BookingResponseDto")
    public void find_byIdCorrect_returnBookingResponse() {
        Booking booking = getBooking();
        BookingResponseDto bookingResponse = getBookingResponseDto();

        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingMapper.toDto(booking))
                .thenReturn(bookingResponse);

        BookingResponseDto actual = bookingService.findById(BOOKING_ID);

        assertEquals(bookingResponse, actual);
    }

    @Test
    @DisplayName("Test findById method with incorrect ID should throw EntityNotFoundException")
    public void find_byIdIncorrect_throwEntityNotFoundException() {
        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.findById(BOOKING_ID));
    }

    @Test
    @DisplayName("Test findUserBookingAll method should return list of BookingResponseDto")
    @WithMockUser(username = "admin",roles = "ADMIN")
    public void find_usersBooking_returnBookingDtoList() {
        Booking booking = getBooking();
        BookingResponseDto bookingResponse = getBookingResponseDto();
        Mockito.when(bookingRepository.findAllByUserId(USER_ID))
                .thenReturn(List.of(booking));
        Mockito.when(bookingMapper.toDto(booking)).thenReturn(bookingResponse);
        List<BookingResponseDto> userBookingAll = bookingService.findUserBookingAll(authentication);

        assertEquals(List.of(bookingResponse), userBookingAll);
    }

    @Test
    @DisplayName("Test updateUserBookingById method with "
            + "correct ID should return updated BookingResponseDto")
    @WithMockUser(username = "admin",roles = "ADMIN")
    public void update_byCorrectId_returnBookingDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 20),
                1L,
                "PENDING"
        );

        Booking booking = getBooking();
        Booking bookingForUpdate = getBooking();
        bookingForUpdate.setCheckOutDate(LocalDate.of(2024, 2, 20));

        BookingResponseDto bookingUpdateResponseDto = getBookingUpdateResponseDto();

        Mockito.when(bookingRepository.findAllByUserId(USER_ID)).thenReturn(List.of(booking));
        Mockito.when(bookingMapper.toModel(USER_ID, bookingRequestDto)).thenReturn(booking);
        Mockito.when(bookingMapper.toDto(any())).thenReturn(bookingUpdateResponseDto);
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(bookingForUpdate);

        BookingResponseDto actual =
                bookingService.updateUserBookingById(USER_ID, bookingRequestDto, authentication);

        assertEquals(bookingUpdateResponseDto, actual);
    }

    @Test
    @DisplayName("Test findAllByUserIdAndBookingStatus method "
            + "should return list of BookingResponseDto")
    public void findAll_byUserIdAndBookingStatus_returnBookingResponseDtoList() {
        Booking booking = getBooking();
        BookingResponseDto bookingResponseDto = getBookingResponseDto();

        Mockito.when(bookingRepository
                        .findAllByUserIdAndBookingStatus(USER_ID, BookingStatus.PENDING))
                .thenReturn(List.of(booking));
        Mockito.when(bookingMapper.toDto(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> actual =
                bookingService.findAllByUserIdAndBookingStatus(USER_ID, "PENDING");

        assertEquals(List.of(bookingResponseDto), actual);
    }

    @Test
    @DisplayName("Test updateStatus method with correct ID should return updated Booking")
    public void updateStatus_byId_returnBookingResponseDto() {
        Booking booking = getBooking();

        Booking bookingUpdateStatus = getBooking();
        bookingUpdateStatus.setBookingStatus(BookingStatus.CONFIRMED);

        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(any()))
                .thenReturn(bookingUpdateStatus);

        Booking actual =
                bookingService.updateStatus(BOOKING_ID, BookingStatus.CONFIRMED);

        assertEquals(bookingUpdateStatus, actual);
    }

    @Test
    @DisplayName("Test updateStatus method with CANCELED Booking should throw BookingException")
    void updateStatus_CanceledBooking_ExceptionThrown() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.CANCELED);

        Mockito.when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        assertThrows(BookingException.class, () ->
                bookingService.updateStatus(bookingId, BookingStatus.CONFIRMED));
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, never()).save(any());
    }

    private Booking getBooking() {
        Booking booking = new Booking(BOOKING_ID);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setAccommodation(new Accommodation(ACCOMMODATION_ID));
        booking.setUser(new User(USER_ID));
        booking.setCheckInDate(LocalDate.of(2024, 2, 1));
        booking.setCheckOutDate(LocalDate.of(2024, 2, 11));
        return booking;
    }

    private BookingResponseDto getBookingResponseDto() {
        return new BookingResponseDto(
                BOOKING_ID,
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 11),
                ACCOMMODATION_ID,
                USER_ID,
                "PENDING");
    }

    private BookingResponseDto getBookingUpdateResponseDto() {
        return new BookingResponseDto(
                BOOKING_ID,
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 20),
                ACCOMMODATION_ID,
                USER_ID,
                "PENDING");
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
}

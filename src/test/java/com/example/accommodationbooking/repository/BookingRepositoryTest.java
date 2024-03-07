package com.example.accommodationbooking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.accommodationbooking.model.Booking;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/delete-all.sql",
        "classpath:database/insert-accommodation.sql",
        "classpath:database/insert-user-booking_repository-test.sql",
        "classpath:database/insert-booking.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class BookingRepositoryTest {
    private static final Long USER_ID = 1L;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Find bookings by user ID should return booking list")
    public void findBookings_ByUserId_returnBookingList() {
        List<Booking> actual = bookingRepository.findAllByUserId(USER_ID);
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Find bookings by user ID and booking status should return booking list")
    public void findBookings_ByUserIdAndBookingStatus_returnBookingList() {
        List<Booking> actual =
                bookingRepository.findAllByUserIdAndBookingStatus(USER_ID, BookingStatus.PENDING);
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Find bookings by booking status and check out date "
            + "is less than or equal should return booking list")
    public void findBookings_ByBookingStatusAndCheckOutDateIsLessThanEqual_return() {
        List<Booking> actual = bookingRepository
                .findAllByBookingStatusAndCheckOutDateIsLessThanEqual(
                        BookingStatus.PENDING,
                        LocalDate.of(2024, 3, 3));
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Find bookings by check out date between should return booking list")
    public void findBookings_ByCheckOutDateBetween_returnBookingList() {
        Long accommodationId = 1L;
        LocalDate in = LocalDate.of(2024,1,1);
        LocalDate out = LocalDate.of(2024,4,1);
        List<Booking> actual =
                bookingRepository.findAllByCheckOutDateBetween(accommodationId, in, out);
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Find bookings by booking status should return booking list")
    public void findBookings_ByBookingStatus_returnBookingList() {
        List<Booking> actual =
                bookingRepository.findBookingByBookingStatus(BookingStatus.PENDING);
        assertThat(actual).hasSize(1);
    }
}

package com.example.accommodationbooking.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbooking.dto.booking.BookingRequestDto;
import com.example.accommodationbooking.model.enumeration.BookingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/insert-accommodation.sql",
        "classpath:database/insert-user.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class BookingControllerTest {
    private static MockMvc mockMvc;
    private static final String BOOKING_ID = "1";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin1", roles = "ADMIN")
    @DisplayName("Save valid BookingRequestDto and return BookingResponseDto")
    public void save_validBookingRequestDto_returnBookingResponseDto() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 20),
                1L,
                BookingStatus.PENDING.name()
        );

        String jsonRequest = objectMapper.writeValueAsString(bookingRequestDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInDate", is("2024-02-01")))
                .andExpect(jsonPath("$.checkOutDate", is("2024-02-20")))
                .andExpect(jsonPath("$.accommodationId", is(1)))
                .andExpect(jsonPath("$.bookingStatus", is("PENDING")));
    }

    @Test
    @WithMockUser(username = "admin1", roles = "ADMIN")
    @Sql(scripts = "classpath:database/insert-booking.sql")
    @DisplayName("Find by ID and return BookingResponseDto")
    public void find_byId_returnBookingResponseDto() throws Exception {
        mockMvc.perform(get("/bookings/" + BOOKING_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.checkInDate", is("2024-02-10")))
                .andExpect(jsonPath("$.checkOutDate", is("2024-02-21")))
                .andExpect(jsonPath("$.accommodationId", is(1)))
                .andExpect(jsonPath("$.bookingStatus", is("PENDING")));
    }

    @Test
    @WithMockUser(username = "admin1", roles = "ADMIN")
    @DisplayName("Update user booking by ID and return BookingResponseDto")
    public void updateUserBooking_byId_returnBookingResponseDto() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDate.of(2024, 2, 10),
                LocalDate.of(2024, 2, 21),
                1L,
                BookingStatus.PENDING.name()
        );

        String jsonRequest = objectMapper.writeValueAsString(bookingRequestDto);

        mockMvc.perform(put("/bookings/" + BOOKING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInDate", is("2024-02-10")))
                .andExpect(jsonPath("$.checkOutDate", is("2024-02-21")))
                .andExpect(jsonPath("$.accommodationId", is(1)))
                .andExpect(jsonPath("$.bookingStatus", is("PENDING")));
    }

    @Test
    @WithMockUser(username = "admin1",roles = "ADMIN")
    @DisplayName("Delete by ID")
    public void delete_byId() throws Exception {
        mockMvc.perform(delete("/bookings/" + BOOKING_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.LocalDate;

@Sql(scripts = {"classpath:database/insert-accommodation.sql",
        "classpath:database/insert-amenties.sql",
        "classpath:database/insert-user.sql",
        "classpath:database/insert-users-roles.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class BookingControllerTest {
    private static final String ACCOMMODATION_ID = "1";
    @Autowired
    private ObjectMapper objectMapper;
    protected static MockMvc mockMvc;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    public void save_validBookingRequestDto_returnBookingResponseDto() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 20),
                1L,
                "PENDING"
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
}

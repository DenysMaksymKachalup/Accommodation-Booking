package com.example.accommodationbooking.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbooking.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbooking.dto.accommodation.AddressDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class AccommodationControllerTest {

    protected static MockMvc mockMvc;
    private static final String ACCOMMODATION_ID = "1";

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
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Save valid AccommodationRequestDto and return AccommodationResponseDto")
    public void save_validAccommodationRequestDto_returnAccommodationResponseDto()
            throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getAccommodationRequestDto());
        mockMvc.perform(post("/accommodations")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("HOUSE")))
                .andExpect(jsonPath("$.address.street", is("street1")))
                .andExpect(jsonPath("$.address.city", is("city1")))
                .andExpect(jsonPath("$.size", is("size1")))
                .andExpect(jsonPath("$.amenities[0]", is("amenities1")))
                .andExpect(jsonPath("$.amenities[1]", is("amenities1")))
                .andExpect(jsonPath("$.dailyRate", is(100.00)))
                .andExpect(jsonPath("$.availability", is(1)));
    }

    @Sql(scripts = {"classpath:database/insert-accommodation.sql",
            "classpath:database/insert-amenties.sql"})
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find by ID and return AccommodationResponseDto")
    public void find_byId_returnAccommodationResponseDto() throws Exception {
        mockMvc.perform(get("/accommodations/" + ACCOMMODATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("HOUSE")))
                .andExpect(jsonPath("$.address.street", is("street1")))
                .andExpect(jsonPath("$.address.city", is("city1")))
                .andExpect(jsonPath("$.size", is("size1")))
                .andExpect(jsonPath("$.amenities[0]", is("amenities1")))
                .andExpect(jsonPath("$.amenities[1]", is("amenities1")))
                .andExpect(jsonPath("$.dailyRate", is(100)))
                .andExpect(jsonPath("$.availability", is(1)));
    }

    @Sql(scripts = {"classpath:database/insert-accommodation.sql",
            "classpath:database/insert-amenties.sql"})
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update by ID and return AccommodationResponseDto")
    public void updated_byId_returnAccommodationResponseDto() throws Exception {
        AddressDto addressDto = new AddressDto("street1", "city1");
        AccommodationRequestDto accommodationRequestDto = new AccommodationRequestDto(
                "HOUSE",
                addressDto,
                "sizeUpdate",
                List.of("amenities1", "amenitiesUpdate"),
                BigDecimal.valueOf(100.00),
                1
        );
        String jsonRequest = objectMapper.writeValueAsString(accommodationRequestDto);

        mockMvc.perform(put("/accommodations/" + ACCOMMODATION_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("HOUSE")))
                .andExpect(jsonPath("$.address.street", is("street1")))
                .andExpect(jsonPath("$.address.city", is("city1")))
                .andExpect(jsonPath("$.size", is("sizeUpdate")))
                .andExpect(jsonPath("$.amenities[0]", is("amenities1")))
                .andExpect(jsonPath("$.amenities[1]", is("amenitiesUpdate")))
                .andExpect(jsonPath("$.dailyRate", is(100.00)))
                .andExpect(jsonPath("$.availability", is(1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete by ID")
    public void delete_byId() throws Exception {
        mockMvc.perform(delete("/accommodations/" + ACCOMMODATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private AccommodationRequestDto getAccommodationRequestDto() {
        AddressDto addressDto = new AddressDto("street1", "city1");
        return new AccommodationRequestDto(
                "HOUSE",
                addressDto,
                "size1",
                List.of("amenities1", "amenities1"),
                BigDecimal.valueOf(100.00),
                1
        );
    }
}

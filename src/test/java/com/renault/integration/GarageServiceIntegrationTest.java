package com.renault.integration;

import com.renault.dto.GarageDTO;
import com.renault.dto.OpeningTimeDTO;
import com.renault.service.GarageService;
import com.renault.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql("/garage_data.sql")
public class GarageServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GarageService garageService;

    @Test
    void testCreateGarage() throws Exception {
        var postGarage = new GarageDTO();
        postGarage.setAddress("address test");
        postGarage.setName("GARAGE NAME");
        postGarage.setEmail("test@test.com");
        postGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        postGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));

        // Execute the POST request
        mockMvc.perform(post("/api/v1/garage/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postGarage)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("GARAGE NAME")))
                .andExpect(jsonPath("$.timeSlots", aMapWithSize(2)))
                .andExpect(jsonPath("$.email", is("test@test.com")));
    }

    @Test
    void testGetGarageByIdFound() throws Exception {
        // Execute the GET request
        mockMvc.perform(get("/api/v1/garage/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Weekend Garage")))
                .andExpect(jsonPath("$.email", is("weekend@garage.com")))
                .andExpect(jsonPath("$.timeSlots", aMapWithSize(3)));

    }
}

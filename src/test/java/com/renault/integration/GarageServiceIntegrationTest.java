package com.renault.integration;

import com.renault.dto.GarageDTO;
import com.renault.dto.OpeningTimeDTO;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql("/garage_data.sql")
public class GarageServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_URI = "/api/v1/garage";

    @Test
    void testCreateGarage() throws Exception {
        //Setup DTO
        var postGarage = new GarageDTO();
        postGarage.setAddress("address test");
        postGarage.setName("GARAGE NAME");
        postGarage.setEmail("test@test.com");
        postGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        postGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));

        // Execute the POST request
        mockMvc.perform(post(BASE_URI + "/save")
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
        mockMvc.perform(get(BASE_URI + "/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Weekend Garage")))
                .andExpect(jsonPath("$.email", is("weekend@garage.com")))
                .andExpect(jsonPath("$.timeSlots", aMapWithSize(3)));

    }

    @Test
    void testGetGarageByIdNotFound() throws Exception {
        // Execute the GET request
        mockMvc.perform(get(BASE_URI + "/{id}", 100))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Garage not found with ID :100")));
    }

    @Test
    void testUpdateGarageSuccess() throws Exception {
        //Setup DTO
        var putGarage = new GarageDTO();
        putGarage.setId(1L);
        putGarage.setAddress("updated address test");
        putGarage.setName("updated garage name");
        putGarage.setEmail("test@test.com");
        putGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        putGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putGarage)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("updated garage name")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.timeSlots", aMapWithSize(2)));

    }

    @Test
    void testUpdateGarageFailure() throws Exception {
        //Setup DTO
        var putGarage = new GarageDTO();
        putGarage.setId(100L);
        putGarage.setAddress("updated address test");
        putGarage.setName("updated garage name");
        putGarage.setEmail("test@test.com");
        putGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        putGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putGarage)))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Garage not found with ID :100")));

    }

    @Test
    void testDeleteGarageSuccess() throws Exception {
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

    }

    @Test
    void testDeleteGarageFailure() throws Exception {
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 100))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Garage not found with ID :100")));
    }

    @Test
    void testGetVehiclesByTypes() throws Exception {
        // Execute the GET request
        mockMvc.perform(get(BASE_URI + "/byTypeVehicle/{typeVehicle}", "COUPE"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].vehicles", hasSize(3)))
                .andExpect(jsonPath("$[0].vehicles[0].typeVehicle", is("COUPE")));
    }

    @Test
    void testGetByAccessory() throws Exception {
        // Execute the GET request
        mockMvc.perform(get(BASE_URI + "/byAccessory/{accessoryName}", "Phone Holder"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].vehicles", hasSize(3)))
                .andExpect(jsonPath("$[0].vehicles[0].accessories[0].name", is("Phone Holder")));
    }
}

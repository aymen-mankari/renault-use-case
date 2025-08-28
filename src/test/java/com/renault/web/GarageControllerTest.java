package com.renault.web;

import com.renault.dto.GarageDTO;
import com.renault.dto.OpeningTimeDTO;
import com.renault.exception.DataNotFoundException;
import com.renault.service.GarageService;
import com.renault.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GarageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GarageService garageService;

    private GarageDTO mockGarage;

    @BeforeEach
    void initializeData(){
        mockGarage = new GarageDTO();
        mockGarage.setId(1L);
        mockGarage.setAddress("address test");
        mockGarage.setName("GARAGE NAME");
        mockGarage.setEmail("test@test.com");
        mockGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        mockGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
    }

    @Test
    void testGetGarageByIdFound() throws Exception {
        // Setup mocks
        doReturn(mockGarage).when(garageService).findById(any());
        // Execute the GET request
        mockMvc.perform(get("/api/v1/garage/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("GARAGE NAME")))
                .andExpect(jsonPath("$.email", is("test@test.com")));
    }

    @Test
    void testGetGarageByIdNotFound() throws Exception {
        // Setup mocks
        doThrow(DataNotFoundException.class).when(garageService).findById(any());
        // Execute the GET request
        mockMvc.perform(get("/api/v1/garage/{id}", 1))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testSaveGarageSuccess() throws Exception {
        // Setup mocks
        doReturn(mockGarage).when(garageService).save(any());
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
    void testSaveGarageFailure() throws Exception {
        // Setup mocks
        doThrow(RuntimeException.class).when(garageService).save(any());
        var mockPostGarage = new GarageDTO();

        // Execute the POST request
        mockMvc.perform(post("/api/v1/garage/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(mockPostGarage)))
                // Validate the returned fields
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(500)));
    }

    @Test
    void testUpdateGarageSuccess() throws Exception {
        // Setup mocks
        var mockUpdatedGarage = new GarageDTO();
        mockUpdatedGarage.setId(1L);
        mockUpdatedGarage.setAddress("updated address test");
        mockUpdatedGarage.setName("UPDATED GARAGE NAME");
        mockUpdatedGarage.setEmail("test@test.com");
        mockUpdatedGarage.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        mockUpdatedGarage.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        mockUpdatedGarage.getTimeSlots().put(DayOfWeek.WEDNESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        mockUpdatedGarage.getTimeSlots().put(DayOfWeek.FRIDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        doReturn(mockUpdatedGarage).when(garageService).update(any());

        // Execute the POST request
        mockMvc.perform(put("/api/v1/garage/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(mockUpdatedGarage)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("UPDATED GARAGE NAME")))
                .andExpect(jsonPath("$.timeSlots", aMapWithSize(4)));
    }


}

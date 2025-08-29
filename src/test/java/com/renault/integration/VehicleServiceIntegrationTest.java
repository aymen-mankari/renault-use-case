package com.renault.integration;

import com.renault.dto.VehicleDTO;
import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql("/garage_data.sql")
public class VehicleServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_URI = "/api/v1/vehicle";

    @Test
    void testSaveVehicle() throws Exception {
        var postVehicle = VehicleDTO.builder()
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();
        // Execute the POST request
        mockMvc.perform(post(BASE_URI + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postVehicle)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.brand", is("Toyota")))
                .andExpect(jsonPath("$.manufactureYear", is(2010)));
    }

    @Test
    void testUpdateVehicleSuccess() throws Exception {
        var postVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Peugeot")
                .typeVehicle(TypeVehicle.SEDANS)
                .manufactureYear(2025)
                .fuelType(FuelType.DIESEL)
                .build();
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postVehicle)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.brand", is("Peugeot")))
                .andExpect(jsonPath("$.fuelType", is("DIESEL")))
                .andExpect(jsonPath("$.manufactureYear", is(2025)));
    }

    @Test
    void testUpdateVehicleFailure() throws Exception {
        var postVehicle = VehicleDTO.builder()
                .id(100L)
                .brand("Renault")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();
        // Execute PUT request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postVehicle)))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Vehicle not found with ID :100")));
    }

    @Test
    void testDeleteVehicleSuccess() throws Exception {
        // Execute DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 2))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testDeleteVehicleFailure() throws Exception {
        // Execute DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 100))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Vehicle not found with ID :100")));
    }

    @Test
    void testAddVehicleToGarageSuccess() throws Exception {
        // Execute PUT request
        mockMvc.perform(put(BASE_URI + "/{vehicleId}/garage/{garageId}", 1, 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testAddVehicleToGarageFailure() throws Exception {
        // Execute PUT request
        mockMvc.perform(put(BASE_URI + "/{vehicleId}/garage/{garageId}", 100, 1))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Vehicle not found with ID :100")));

    }

    @Test
    void testRemoveVehicleFromGarageSuccess() throws Exception {
        // Execute DELETE request
        mockMvc.perform(delete(BASE_URI + "/{vehicleId}/garage/{garageId}", 1, 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    @Test
    void testRemoveVehicleFromGarage_WhenVehicleDoesNotBelongToGarage() throws Exception {
        // Execute DELETE request
        mockMvc.perform(delete(BASE_URI + "/{vehicleId}/garage/{garageId}", 5, 1))
                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(400)))
                .andExpect(jsonPath("$.message", containsString("Vehicle [5] doesn't belong to this garage [1]")));
    }

    @Test
    void testGetVehiclesRelatedToGarage() throws Exception {
        // Execute GET request
        mockMvc.perform(get(BASE_URI + "/garage/{garageId}", 1))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void testGetVehiclesAssociatedToGaragesByBrand() throws Exception {
        // Execute GET request
        mockMvc.perform(get(BASE_URI + "/brand/{brand}", "Renault"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vehicles", hasSize(2)))
                .andExpect(jsonPath("$[0].vehicles[0].brand", is("Renault")));
    }
}

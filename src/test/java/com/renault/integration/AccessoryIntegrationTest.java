package com.renault.integration;

import com.renault.dto.AccessoryDTO;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql("/garage_data.sql")
public class AccessoryIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_URI = "/api/v1/accessory";

    @Test
    void testSaveAccessorySuccess() throws Exception {
        var postAccessory = AccessoryDTO.builder()
                .name("accessory name")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("description of the accessory")
                .build();
        // Execute the POST request
        mockMvc.perform(post(BASE_URI + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postAccessory)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("accessory name")))
                .andExpect(jsonPath("$.price", is(120.99)));
    }

    @Test
    void testUpdateAccessorySuccess() throws Exception {
        var putAccessory = AccessoryDTO.builder()
                .id(2L)
                .name("Updated Phone Holder")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("updated description accessory 1")
                .build();

        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putAccessory)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Updated Phone Holder")))
                .andExpect(jsonPath("$.price", is(120.99)));
    }

    @Test
    void testUpdateAccessory_WhenAccessoryNotFound() throws Exception {
        var putAccessory = AccessoryDTO.builder()
                .id(100L)
                .name("Updated Phone Holder")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("updated description accessory 1")
                .build();

        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putAccessory)))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Accessory not found with ID : 100")));

    }

    @Test
    void testDeleteAccessorySuccess() throws Exception {
        final var accessoryId = 1L;
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", accessoryId))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testDeleteAccessory_WhenAccessoryNotFound() throws Exception {
        final var accessoryId = 100L;
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", accessoryId))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Accessory not found with ID : 100")));

    }

    @Test
    void testAddAccessoryToVehicleSuccess() throws Exception {
        final var accessoryId = 3L;
        final var vehicleId = 1L;
        mockMvc.perform(put(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testAddAccessoryToVehicle_WhenAccessoryAlreadyExistInVehicle() throws Exception {
        final var accessoryId = 2L;
        final var vehicleId = 1L;
        mockMvc.perform(put(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus", is(400)))
                .andExpect(jsonPath("$.message", containsString("Accessory [2] already found in the vehicle accessories list")));

    }

    @Test
    void testAddAccessoryToVehicle_WhenAccessoryNotFound() throws Exception {
        final var accessoryId = 100L;
        final var vehicleId = 1L;
        mockMvc.perform(put(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus", is(404)))
                .andExpect(jsonPath("$.message", containsString("Accessory not found with ID : 100")));

    }

    @Test
    void testRemoveAccessoryFromVehicleSuccess() throws Exception {
        final var accessoryId = 2L;
        final var vehicleId = 1L;
        mockMvc.perform(delete(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testRemoveAccessoryFromVehicle_WhenAccessoryDoesNotExistInVehicle() throws Exception {
        final var accessoryId = 3L;
        final var vehicleId = 1L;
        mockMvc.perform(delete(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus", is(400)))
                .andExpect(jsonPath("$.message", containsString("Accessory [3] not found in the vehicle accessories list")));

    }

    @Test
    void testRemoveAccessoryFromVehicle_WhenAccessoryNotFound() throws Exception {
        final var accessoryId = 30L;
        final var vehicleId = 1L;
        mockMvc.perform(put(BASE_URI + "/{accessoryId}/vehicle/{vehicleId}", accessoryId, vehicleId))
                // Validate results
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testGetAccessoriesForVehicleSuccess() throws Exception {
        final var vehicleId = 1L;
        mockMvc.perform(get(BASE_URI + "/vehicle/{vehicleId}", vehicleId))
                // Validate results
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetAccessoriesForVehicleFailure() throws Exception {
        final var vehicleId = 100L;
        mockMvc.perform(get(BASE_URI + "/vehicle/{vehicleId}", vehicleId))
                // Validate results
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }
}

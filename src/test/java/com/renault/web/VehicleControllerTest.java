package com.renault.web;

import com.renault.dto.GarageDTO;
import com.renault.dto.VehicleDTO;
import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import com.renault.exception.DataNotFoundException;
import com.renault.service.VehicleService;
import com.renault.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private VehicleService mockVehicleService;

    private static final String BASE_URI = "/api/v1/vehicle";

    @Test
    void testSaveVehicleSuccess() throws Exception {
        var mockVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();

        var postVehicle = VehicleDTO.builder()
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();

        doReturn(mockVehicle).when(mockVehicleService).save(any());
        // Execute the GET request
        mockMvc.perform(post(BASE_URI + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postVehicle)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.brand", is("Toyota")))
                .andExpect(jsonPath("$.manufactureYear", is(2010)));
    }

    @Test
    void testSaveVehicleFailure() throws Exception {
        var postVehicle = VehicleDTO.builder()
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();

        doThrow(RuntimeException.class).when(mockVehicleService).save(any());
        // Execute the GET request
        mockMvc.perform(post(BASE_URI + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(postVehicle)))
                // Validate the returned fields
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(500)));
    }

    @Test
    void testUpdateVehicleSuccess() throws Exception {
        var mockVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Porsche")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2025)
                .fuelType(FuelType.HYBRIDE)
                .build();

        var putVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Porsche")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2025)
                .fuelType(FuelType.HYBRIDE)
                .build();

        doReturn(mockVehicle).when(mockVehicleService).update(any());
        // Execute the GET request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putVehicle)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.brand", is("Porsche")))
                .andExpect(jsonPath("$.manufactureYear", is(2025)));
    }

    @Test
    void testUpdateVehicle_WhenVehicleNotFound() throws Exception {
        var putVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Porsche")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2025)
                .fuelType(FuelType.HYBRIDE)
                .build();

        doThrow(DataNotFoundException.class).when(mockVehicleService).update(any());
        // Execute the GET request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putVehicle)))
                // Validate the returned fields
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testUpdateVehicle_WhenRunTimeExceptionOccurs() throws Exception {
        var putVehicle = VehicleDTO.builder()
                .id(1L)
                .brand("Porsche")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2025)
                .fuelType(FuelType.HYBRIDE)
                .build();

        doThrow(RuntimeException.class).when(mockVehicleService).update(any());
        // Execute the GET request
        mockMvc.perform(put(BASE_URI + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(putVehicle)))
                // Validate the returned fields
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(500)));
    }

    @Test
    void testDeleteVehicleSuccess() throws Exception {
        // Setup mocks
        doReturn(Boolean.valueOf(true)).when(mockVehicleService).delete(any());
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testDeleteVehicleFailure() throws Exception {
        // Setup mocks
        doThrow(DataNotFoundException.class).when(mockVehicleService).delete(any());
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/delete/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testAddVehicleToGarageSuccess() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doReturn(true).when(mockVehicleService).addVehicleToGarage(vehicleId, garageId);
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testAddVehicleToGarage_WhenDataNotFoundExceptionOccurs() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doThrow(DataNotFoundException.class).when(mockVehicleService).addVehicleToGarage(vehicleId, garageId);
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testAddVehicleToGarage_WhenRuntimeExceptionOccurs() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doThrow(RuntimeException.class).when(mockVehicleService).addVehicleToGarage(vehicleId, garageId);
        // Execute the PUT request
        mockMvc.perform(put(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(500)));
    }

    @Test
    void testRemoveVehicleFromGarageSuccess() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doReturn(true).when(mockVehicleService).removeVehicleFromGarage(vehicleId, garageId);
        // Execute the PUT request
        mockMvc.perform(delete(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testRemoveVehicleFromGarage_WhenDataNotFoundExceptionOccurs() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doThrow(DataNotFoundException.class).when(mockVehicleService).removeVehicleFromGarage(vehicleId, garageId);
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testRemoveVehicleFromGarage_WhenRuntimeExceptionOccurs() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doThrow(RuntimeException.class).when(mockVehicleService).removeVehicleFromGarage(vehicleId, garageId);
        // Execute the DELETE request
        mockMvc.perform(delete(BASE_URI + "/{vehicleId}/garage/{garageId}", vehicleId, garageId))
                // Validate the returned fields
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(500)));
    }

    @Test
    void testGetVehiclesRelatedToGarageSuccess() throws Exception {
        //Setup mocks
        final var garageId = 200L;
        var mockVehicle1 = VehicleDTO.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();
        var mockVehicle2 = VehicleDTO.builder()
                .id(2L)
                .brand("Renault")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2015)
                .fuelType(FuelType.DIESEL)
                .build();
        final Set<VehicleDTO> mockSetVehicles = Set.of(mockVehicle1, mockVehicle2);
        doReturn(mockSetVehicles).when(mockVehicleService).getVehiclesRelatedToAGarage(any());
        mockMvc.perform(get(BASE_URI + "/garage/{garageId}", garageId))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetVehiclesRelatedToGarage_WhenDataNotFoundExceptionOccurs() throws Exception {
        //Setup mock
        final var vehicleId = 100L;
        final var garageId = 200L;
        doThrow(DataNotFoundException.class).when(mockVehicleService).getVehiclesRelatedToAGarage(garageId);
        // Execute the GET request
        mockMvc.perform(get(BASE_URI + "/garage/{garageId}", garageId))
                // Validate the returned fields
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus", is(404)));
    }

    @Test
    void testGetVehiclesAssociatedToGaragesByBrandSuccess() throws Exception {
        //Setup mock
        final var brandName = "Toyota";
        final var mockSetGarage= Set.of(mock(GarageDTO.class), mock(GarageDTO.class));
        doReturn(mockSetGarage).when(mockVehicleService).getVehiclesByBrandAssociatedToGarages(brandName);
        // Execute the GET request
        mockMvc.perform(get(BASE_URI + "/brand/{brand}", brandName))
                // Validate the returned fields
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vehicles").exists());
    }
}

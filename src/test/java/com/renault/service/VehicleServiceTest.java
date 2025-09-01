package com.renault.service;

import com.renault.constants.ApplicationConstants;
import com.renault.dto.VehicleDTO;
import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import com.renault.exception.BadRequestException;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Garage;
import com.renault.model.Vehicle;
import com.renault.repository.GarageRepository;
import com.renault.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
    @MockitoBean
    private VehicleRepository mockVehicleRepository;
    @MockitoBean
    private GarageRepository mockGarageRepository;
    @InjectMocks
    private VehicleService vehicleService;
    @Mock
    private Garage mockGarage;
    @Mock
    private Vehicle mockVehicle;
    @Mock
    private VehicleDTO mockVehicleDTO;

    @Test
    void testSaveVehicleSuccess() {
        //Setup mocks
        mockVehicle = Vehicle.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();
        mockVehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .typeVehicle(TypeVehicle.SUV)
                .manufactureYear(2010)
                .fuelType(FuelType.HYBRIDE)
                .build();
        doReturn(mockVehicle).when(mockVehicleRepository).save(any());
        //Invoking save method
        var result = vehicleService.save(mockVehicleDTO);
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(VehicleDTO.class, result);
        Assertions.assertEquals("Toyota", result.getBrand());
    }

    @Test
    void updateVehicleSuccess() {
        //Setting mocks
        var mockUpdatedVehicleDTO = VehicleDTO.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();
        var mockUpdatedVehicle = Vehicle.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();
        doReturn(true).when(mockVehicleRepository).existsById(any());
        doReturn(mockUpdatedVehicle).when(mockVehicleRepository).save(any());
        //Invoking update method
        var result = vehicleService.update(mockUpdatedVehicleDTO);
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertSame("Toyota", result.getBrand());
        Assertions.assertSame(FuelType.HYBRIDE, result.getFuelType());
    }

    @Test
    void deleteVehicleSuccess() {
        //Setting mocks
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        //Invoking delete method
        var result = vehicleService.delete(1L);
        //Verify results
        Assertions.assertTrue(result);
    }

    @Test
    void deleteVehicleFailure() {
        //Setup mocks
        doReturn(Optional.empty()).when(mockVehicleRepository).findById(any());
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> vehicleService.delete(1L));
    }

    @Test
    void testAddVehicleToGarageSuccess() {
        //Setting mocks
        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(mockVehicle).when(mockVehicleRepository).save(any());
        //Invoking service method
        final var vehicleId = 100L;
        final var garageId = 1L;
        var result = vehicleService.addVehicleToGarage(vehicleId, garageId);
        //Verify results
        Assertions.assertTrue(result);
        verify(mockVehicle).addGarage(mockGarage);
    }

    @Test
    void testAddVehicleToGarageFailure() {
        //Setup mocks
        doReturn(Optional.empty()).when(mockGarageRepository).findById(any());
        final var vehicleId = 100L;
        final var garageId = 1L;
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> vehicleService.addVehicleToGarage(vehicleId, garageId));
    }

    @Test
    void testAddVehicleToGarage_WhenGarageReachedMaxSize() {
        //Setup mocks
        var mockVehicle = Vehicle.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();

        Garage spyGarage = spy(Garage.class);
        Set<Vehicle> mockVehicleSet = mock(Set.class);
        doReturn(50).when(mockVehicleSet).size();
        doReturn(mockVehicleSet).when(spyGarage).getVehicles();

        doReturn(Optional.of(spyGarage)).when(mockGarageRepository).findById(any());
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());

        final var vehicleId = 100L;
        final var garageId = 1L;
        //Invoke method & Verify results
        Assertions.assertThrows(BadRequestException.class, () -> vehicleService.addVehicleToGarage(vehicleId, garageId));
    }

    @Test
    void testRemoveVehicleFromGarageSuccess() {
        //Setting mocks
        var mockVehicle = Vehicle.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();
        Garage mockGarage = mock(Garage.class);
        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(mockVehicle).when(mockVehicleRepository).save(any());

        //Invoking service method
        final var vehicleId = 100L;
        final var garageId = 1L;
        var result = vehicleService.removeVehicleFromGarage(vehicleId, garageId);
        //Verify results
        Assertions.assertTrue(result);
        verify(mockGarage).removeVehicle(mockVehicle);
    }

    @Test
    void testRemoveVehicleFromGarageFailure() {
        //Setting mocks
        doReturn(Optional.empty()).when(mockGarageRepository).findById(any());
        //Invoking service method
        final var vehicleId = 100L;
        final var garageId = 1L;
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> vehicleService.removeVehicleFromGarage(vehicleId, garageId));
    }

    @Test
    void testGetVehiclesRelatedToAGarage() {
        //Setup mocks
        var idGarage = 1L;
        var mockVehicle1 = Vehicle.builder()
                .id(1L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();
        var mockVehicle2 = Vehicle.builder()
                .id(2L)
                .brand("Toyota")
                .typeVehicle(TypeVehicle.COUPE)
                .manufactureYear(2020)
                .fuelType(FuelType.HYBRIDE)
                .build();
        var mockSetVehicles = Set.of(mockVehicle1, mockVehicle2);
        var mockGarage = mock(Garage.class);
        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        when(mockGarage.getVehicles()).thenReturn(mockSetVehicles);
        //Invoke service method
        var result = vehicleService.getVehiclesRelatedToAGarage(idGarage);
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}

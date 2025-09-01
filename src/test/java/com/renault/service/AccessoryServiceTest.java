package com.renault.service;

import com.renault.dto.AccessoryDTO;
import com.renault.dto.VehicleDTO;
import com.renault.exception.BadRequestException;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Accessory;
import com.renault.model.Vehicle;
import com.renault.repository.AccessoryRepository;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class AccessoryServiceTest {

    @MockitoBean
    private VehicleRepository mockVehicleRepository;
    @MockitoBean
    private AccessoryRepository mockAccessoryRepository;
    @InjectMocks
    private AccessoryService accessoryService;

    @Mock
    private Vehicle mockVehicle;
    @Mock
    private Accessory mockAccessory;
    @Mock
    private AccessoryDTO mockAccessoryDTO;

    @Test
    void testSaveAccessorySuccess() {
        //Setup mocks
        mockAccessory = Accessory.builder()
                .id(1L)
                .name("accessory name")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("description of the accessory")
                .build();
        mockAccessoryDTO = AccessoryDTO.builder()
                .name("accessory name")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("description of the accessory")
                .build();
        doReturn(mockAccessory).when(mockAccessoryRepository).save(any());
        //Invoking save method
        var result = accessoryService.save(mockAccessoryDTO);
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("accessory name", result.getName());
        Assertions.assertEquals(BigDecimal.valueOf(120.99), result.getPrice());
    }

    @Test
    void testSaveAccessoryFailure() {
        //Setup mocks
        mockAccessory = Accessory.builder()
                .id(1L)
                .name("accessory name")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("description of the accessory")
                .build();
        mockAccessoryDTO = AccessoryDTO.builder()
                .name("accessory name")
                .price(BigDecimal.valueOf(120.99))
                .type("specific accessory type")
                .description("description of the accessory")
                .build();
        doThrow(RuntimeException.class).when(mockAccessoryRepository).save(any());
        //Invoking save method & Verify results
        Assertions.assertThrows(RuntimeException.class, () -> accessoryService.save(mockAccessoryDTO));
    }

    @Test
    void testUpdateAccessorySuccess() {
        //Setup mocks
        mockAccessory = Accessory.builder()
                .id(1L)
                .name("updated accessory name")
                .price(BigDecimal.valueOf(150.99))
                .type("updated specific accessory type")
                .description("updated description of the accessory")
                .build();
        mockAccessoryDTO = AccessoryDTO.builder()
                .id(1L)
                .name("updated accessory name")
                .price(BigDecimal.valueOf(150.99))
                .type("updated specific accessory type")
                .description("updated description of the accessory")
                .build();
        doReturn(true).when(mockAccessoryRepository).existsById(any());
        doReturn(mockAccessory).when(mockAccessoryRepository).save(any());
        //Invoking save method
        var result = accessoryService.update(mockAccessoryDTO);
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("updated accessory name", result.getName());
        Assertions.assertEquals(BigDecimal.valueOf(150.99), result.getPrice());
    }

    @Test
    void testUpdateAccessoryFailure() {
        //Setup mock
        doReturn(false).when(mockAccessoryRepository).existsById(any());
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> accessoryService.update(mockAccessoryDTO));
    }

    @Test
    void deleteAccessorySuccess() {
        //Setup mock
        doReturn(Optional.of(mockAccessory)).when(mockAccessoryRepository).findById(any());
        //Invoking delete method
        var result = accessoryService.delete(1L);
        //Verify results
        Assertions.assertTrue(result);
    }

    @Test
    void deleteAccessoryFailure() {
        //Setup mocks
        doReturn(Optional.empty()).when(mockAccessoryRepository).findById(any());
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> accessoryService.delete(1L));
    }

    @Test
    void testAddAccessoryToVehicleSuccess() {
        //Setup mock
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(Optional.of(mockAccessory)).when(mockAccessoryRepository).findById(any());
        doReturn(mockAccessory).when(mockAccessoryRepository).save(any());

        final Long vehicleId = 100L;
        final Long accessoryId = 200L;
        //Invoke method
        var result = accessoryService.addAccessoryToVehicle(vehicleId, accessoryId);
        //Verify results
        Assertions.assertTrue(result);
        verify(mockVehicle).addAccessory(any());
    }

    @Test
    void testAddAccessoryToVehicleFailure() {
        //Setup mock
        doReturn(Optional.empty()).when(mockVehicleRepository).findById(any());
        final Long vehicleId = 100L;
        final Long accessoryId = 200L;
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> accessoryService.addAccessoryToVehicle(vehicleId, accessoryId));
    }

    @Test
    void testRemoveAccessoryFromVehicleSuccess() {
        //Setup mock
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(Optional.of(mockAccessory)).when(mockAccessoryRepository).findById(any());
        doReturn(mockAccessory).when(mockAccessoryRepository).save(any());
        final Long vehicleId = 100L;
        final Long accessoryId = 200L;
        //Invoke method
        var result = accessoryService.removeAccessoryFromVehicle(vehicleId, accessoryId);
        //Verify results
        Assertions.assertTrue(result);
        verify(mockVehicle).removeAccessory(any());
    }

    @Test
    void testRemoveAccessoryFromVehicleFailure() {
        //Setup mock
        doReturn(Optional.empty()).when(mockVehicleRepository).findById(any());
        final Long vehicleId = 100L;
        final Long accessoryId = 200L;
        //Invoke method & Verify results
        Assertions.assertThrows(DataNotFoundException.class, () -> accessoryService.removeAccessoryFromVehicle(vehicleId, accessoryId));

    }

    @Test
    void testRemoveAccessoryToVehicle_WhenAccessoryDoesNotExist() {
        //Setup mock
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(Optional.of(mockAccessory)).when(mockAccessoryRepository).findById(any());
        doThrow(BadRequestException.class).when(mockVehicle).removeAccessory(any());
        final Long vehicleId = 100L;
        final Long accessoryId = 200L;
        //Invoke method & Verify results
        Assertions.assertThrows(BadRequestException.class, () -> accessoryService.removeAccessoryFromVehicle(vehicleId, accessoryId));
    }

    @Test
    void testGetListAccessoriesForAVehicle() {
        //Setup mock
        var mockAccessory1 = Accessory.builder()
                .id(1L)
                .name("updated accessory name")
                .price(BigDecimal.valueOf(150.99))
                .type("updated specific accessory type")
                .description("updated description of the accessory")
                .build();
        var mockAccessory2 = Accessory.builder()
                .id(2L)
                .name("updated accessory name")
                .price(BigDecimal.valueOf(150.99))
                .type("updated specific accessory type")
                .description("updated description of the accessory")
                .build();
        Set<Accessory> mockAccessorySet = Set.of(mockAccessory1, mockAccessory2);

        //mockVehicle.setAccessories(accessories);
        doReturn(Optional.of(mockVehicle)).when(mockVehicleRepository).findById(any());
        doReturn(mockAccessorySet).when(mockVehicle).getAccessories();

        //Invoke method
        final Long vehicleId = 100L;
        var result = accessoryService.getListAccessoriesForAVehicle(vehicleId);
        //Verify results
        Assertions.assertInstanceOf(Set.class, result);
        Assertions.assertEquals(2, result.size());
    }
    
}

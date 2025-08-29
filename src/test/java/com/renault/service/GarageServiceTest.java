package com.renault.service;

import com.renault.dto.GarageDTO;
import com.renault.dto.OpeningTimeDTO;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Garage;
import com.renault.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class GarageServiceTest {
    @MockitoBean
    private GarageRepository mockGarageRepository;
    @InjectMocks
    private GarageService garageService;
    private GarageDTO mockGarageDTO;
    private Garage mockGarage;

    @BeforeEach
    void initializeData() {
        //Initializing common mock objects
        mockGarageDTO = new GarageDTO();
        mockGarageDTO.setAddress("address test");
        mockGarageDTO.setName("GARAGE NAME");
        mockGarageDTO.setEmail("test@test.com");
        mockGarageDTO.getTimeSlots().put(DayOfWeek.MONDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));
        mockGarageDTO.getTimeSlots().put(DayOfWeek.TUESDAY, new OpeningTimeDTO(LocalTime.of(9, 00), LocalTime.of(19, 00)));

        mockGarage = new Garage();
        mockGarage.setId(1L);
        mockGarage.setAddress("address test");
        mockGarage.setName("GARAGE NAME");
        mockGarage.setEmail("test@test.com");
        mockGarage.addTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
    }

    @Test
    void testSave() {
        //Setup mocks
        doReturn(mockGarage).when(mockGarageRepository).save(any());
        //Invoke service method
        var result = this.garageService.save(mockGarageDTO);
        //Verify results
        assertNotNull(result);
        assertInstanceOf(GarageDTO.class, result);
    }

    @Test
    void testUpdateSuccess() {
        //Setup mocks
        var mockUpdatedGarage = new Garage();
        mockUpdatedGarage.setId(1L);
        mockUpdatedGarage.setAddress("updated address");
        mockUpdatedGarage.setName("updated garage name");
        mockUpdatedGarage.setEmail("test@test.com");
        mockUpdatedGarage.addTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockUpdatedGarage.addTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockUpdatedGarage.addTimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));

        doReturn(true).when(mockGarageRepository).existsById(any());
        doReturn(mockUpdatedGarage).when(mockGarageRepository).save(any());
        //Invoke service method
        var result = this.garageService.update(mockGarageDTO);
        //Verify results
        assertInstanceOf(GarageDTO.class, result);
        assertEquals("updated garage name", result.getName());
        assertEquals(3, result.getTimeSlots().size());
    }

    @Test
    void testUpdateFailure() {
        doReturn(Optional.empty()).when(mockGarageRepository).findById(any());
        assertThrows(DataNotFoundException.class, () -> this.garageService.update(mockGarageDTO));
    }

    @Test
    void testFindByIdSuccess() {
        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        var returnedGarage = this.garageService.findById(any());
        assertInstanceOf(GarageDTO.class, returnedGarage);
        assertEquals(1L, mockGarage.getId());
    }

    @Test
    void testFindByIdNotFound() {
        doReturn(Optional.empty()).when(mockGarageRepository).findById(any());
        assertThrows(DataNotFoundException.class,()->this.garageService.findById(any()));
    }

    @Test
    void testDeleteSuccess() {
        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        assertTrue(this.garageService.delete(1L));
    }

    @Test
    void testDeleteFailure() {
        doReturn(Optional.empty()).when(mockGarageRepository).findById(any());
        assertThrows(DataNotFoundException.class, () -> this.garageService.delete(10L));
    }


}

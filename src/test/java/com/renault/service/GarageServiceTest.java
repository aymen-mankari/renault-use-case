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
        doReturn(mockGarage).when(mockGarageRepository).save(any());
        var savedGarage = this.garageService.save(mockGarageDTO);
        assertInstanceOf(GarageDTO.class, savedGarage);
    }

    @Test
    void testUpdateSuccess() {
        var updatedGarage = new Garage();
        updatedGarage.setId(1L);
        updatedGarage.setAddress("updated address");
        updatedGarage.setName("updated garage name");
        updatedGarage.setEmail("test@test.com");
        updatedGarage.addTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        updatedGarage.addTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        updatedGarage.addTimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));

        doReturn(Optional.of(mockGarage)).when(mockGarageRepository).findById(any());
        doReturn(updatedGarage).when(mockGarageRepository).save(any());
        var result = this.garageService.update(mockGarageDTO);
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

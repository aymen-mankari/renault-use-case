package com.renault.repository;

import com.renault.model.Garage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.DayOfWeek;
import java.time.LocalTime;

@DataJpaTest
public class GarageRepositoryTest {

    @Autowired
    private GarageRepository garageRepository;

    private Garage mockGarage;

    @BeforeEach
    void initializeData() {
        //defining Garage objects
        mockGarage = new Garage();
        mockGarage.setAddress("address test");
        mockGarage.setName("GARAGE NAME");
        mockGarage.setEmail("test@test.com");
        mockGarage.addTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.THURSDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        mockGarage.addTimeSlot(DayOfWeek.SATURDAY, LocalTime.of(10, 00), LocalTime.of(17, 00));
        mockGarage.addTimeSlot(DayOfWeek.SUNDAY, LocalTime.of(10, 00), LocalTime.of(17, 00));

    }

    @Test
    void saveTest() {
        var savedGarage = garageRepository.save(mockGarage);
        // Validate the saved garage
        Assertions.assertNotNull(savedGarage);
        Assertions.assertEquals("GARAGE NAME", savedGarage.getName());
        Assertions.assertEquals("test@test.com", savedGarage.getEmail());
        Assertions.assertEquals(7, savedGarage.getTimeSlots().size());
    }

    @Test
    void updateTest() {
        garageRepository.save(mockGarage);
        var garageToUpdate = garageRepository.findById(mockGarage.getId()).get();
        Assertions.assertNotNull(garageToUpdate);

        // Updating data
        var timeSlots = garageToUpdate.getTimeSlots();
        var timeSlotToUpdate = timeSlots.get(DayOfWeek.MONDAY);
        timeSlotToUpdate.setStartTime(LocalTime.of(8, 00));
        garageToUpdate.setPhone("+1-555-0789");
        garageToUpdate.setName("Updated name");

        // Save changes
        var updatedGarage = garageRepository.save(garageToUpdate);

        // Validate the updated garage
        Assertions.assertEquals("Updated name", updatedGarage.getName());
        Assertions.assertEquals("+1-555-0789", updatedGarage.getPhone());
        Assertions.assertEquals(7, updatedGarage.getTimeSlots().size());
        Assertions.assertEquals(LocalTime.of(8, 00), updatedGarage.getTimeSlots().get(DayOfWeek.MONDAY).getStartTime());
    }

    @Test
    void findGarageByIdTest() {
        garageRepository.save(mockGarage);
        var result = garageRepository.findById(mockGarage.getId()).get();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockGarage.getId(), result.getId());
    }

}

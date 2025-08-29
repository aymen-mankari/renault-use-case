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

    private Garage garage;

    @BeforeEach
    void setUp() {
        //Initialize common mock objects
        garage = new Garage();
        garage.setAddress("address test");
        garage.setName("GARAGE NAME");
        garage.setEmail("test@test.com");
        garage.addTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        garage.addTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        garage.addTimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        garage.addTimeSlot(DayOfWeek.THURSDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        garage.addTimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 00), LocalTime.of(19, 00));
        garage.addTimeSlot(DayOfWeek.SATURDAY, LocalTime.of(10, 00), LocalTime.of(17, 00));
        garage.addTimeSlot(DayOfWeek.SUNDAY, LocalTime.of(10, 00), LocalTime.of(17, 00));
    }

    @Test
    void saveTest() {
        //Invoke DB statement
        var result = garageRepository.save(garage);
        // Validate the saved garage
        Assertions.assertNotNull(result);
        Assertions.assertEquals("GARAGE NAME", result.getName());
        Assertions.assertEquals("test@test.com", result.getEmail());
        Assertions.assertEquals(7, result.getTimeSlots().size());
    }

    @Test
    void updateTest() {
        //save a garage to update it later
        garageRepository.save(garage);
        //get garage to update
        var garageToUpdate = garageRepository.findById(garage.getId()).get();
        Assertions.assertNotNull(garageToUpdate);

        // Updating data
        var timeSlots = garageToUpdate.getTimeSlots();
        var timeSlotToUpdate = timeSlots.get(DayOfWeek.MONDAY);
        timeSlotToUpdate.setStartTime(LocalTime.of(8, 00));
        garageToUpdate.setPhone("+1-555-0789");
        garageToUpdate.setName("Updated name");

        // Save changes
        var result = garageRepository.save(garageToUpdate);

        // Validate the updated garage
        Assertions.assertEquals("Updated name", result.getName());
        Assertions.assertEquals("+1-555-0789", result.getPhone());
        Assertions.assertEquals(7, result.getTimeSlots().size());
        Assertions.assertEquals(LocalTime.of(8, 00), result.getTimeSlots().get(DayOfWeek.MONDAY).getStartTime());
    }

    @Test
    void findGarageByIdTest() {
        //save a garage for retrieve
        garageRepository.save(garage);
        //Invoke DB statement
        var result = garageRepository.findById(garage.getId()).get();
        //Verify results
        Assertions.assertNotNull(result);
        Assertions.assertEquals(garage.getId(), result.getId());
    }

}

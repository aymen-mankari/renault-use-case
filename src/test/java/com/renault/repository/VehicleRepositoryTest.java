package com.renault.repository;

import com.renault.model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;
    private Vehicle mockVehicle;

    @BeforeEach
    void initializeData(){
        mockVehicle = new Vehicle();
        mockVehicle.setBrand("Toyota");
        mockVehicle.setManufactureYear(LocalDate.of(2010, 01, 01));
        mockVehicle.setFuelType(Vehicle.FuelType.DIESEL);
    }

    @Test
    void saveTest(){
        vehicleRepository.save(mockVehicle);
        Assertions.assertNotNull(mockVehicle);
        Assertions.assertNotNull(mockVehicle.getId());
    }

    @Test
    void updateTest(){
        vehicleRepository.save(mockVehicle);
        var vehicleToUpdate = vehicleRepository.findById(mockVehicle.getId()).get();
        vehicleToUpdate.setFuelType(Vehicle.FuelType.ELECTRIQUE);
        var updatedVehicle = vehicleRepository.save(vehicleToUpdate);
        Assertions.assertEquals(mockVehicle.getId(), updatedVehicle.getId());
        Assertions.assertEquals(Vehicle.FuelType.ELECTRIQUE, updatedVehicle.getFuelType());
    }
}

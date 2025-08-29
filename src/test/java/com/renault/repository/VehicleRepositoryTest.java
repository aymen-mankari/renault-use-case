package com.renault.repository;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import com.renault.model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;
    private Vehicle vehicle;

    @BeforeEach
    void initializeData(){
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setTypeVehicle(TypeVehicle.COUPE);
        vehicle.setManufactureYear(2010);
        vehicle.setFuelType(FuelType.DIESEL);
    }

    @Test
    void saveTest(){
        var result = vehicleRepository.save(vehicle);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    void updateTest(){
        //save a vehicle to update it later
        vehicleRepository.save(vehicle);
        //get vehicle to update by its id
        var vehicleToUpdate = vehicleRepository.findById(vehicle.getId()).get();

        vehicleToUpdate.setBrand("Renault");
        vehicleToUpdate.setTypeVehicle(TypeVehicle.COUPE);
        vehicleToUpdate.setFuelType(FuelType.ELECTRIQUE);
        //Save changes
        var result = vehicleRepository.save(vehicleToUpdate);
        //Verify results
        Assertions.assertEquals(vehicle.getId(), result.getId());
        Assertions.assertEquals(FuelType.ELECTRIQUE, result.getFuelType());
        Assertions.assertEquals("Renault", result.getBrand());

    }
}

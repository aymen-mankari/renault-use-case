package com.renault.repository;

import com.renault.model.Garage;
import com.renault.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query(value = "SELECT g FROM Garage g JOIN FETCH g.vehicles v WHERE v.brand = :brand")
    Set<Garage> findVehiclesByBrandAssociatedToGarages(@Param("brand") String brandVehicle);
}

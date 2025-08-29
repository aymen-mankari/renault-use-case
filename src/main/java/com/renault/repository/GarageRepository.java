package com.renault.repository;

import com.renault.enums.TypeVehicle;
import com.renault.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    @Query(value = "SELECT g FROM Garage g JOIN FETCH g.vehicles v WHERE v.typeVehicle = :typeVehicle")
    Set<Garage> findByTypeVehicles(@Param("typeVehicle") TypeVehicle typeVehicle);

    @Query(value = "SELECT g FROM Garage g JOIN FETCH g.vehicles v JOIN FETCH v.accessories acc WHERE acc.name = :accessoryName")
    Set<Garage> findByAccessoryName(@Param("accessoryName") String accessoryName);
}

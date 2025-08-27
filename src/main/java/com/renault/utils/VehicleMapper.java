package com.renault.utils;

import com.renault.dto.VehicleDTO;
import com.renault.model.Vehicle;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
@Component
public interface VehicleMapper {
    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);
    Vehicle vehicleDTOtoVehicle(VehicleDTO vehicleDTO);
    Set<VehicleDTO> toVehicleDTOList(Set<Vehicle> vehicleSet);
}

package com.renault.utils;

import com.renault.dto.GarageDTO;
import com.renault.dto.VehicleDTO;
import com.renault.model.Garage;
import com.renault.model.Vehicle;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring")
@Component
public interface GarageMapper {
    GarageDTO garageToGarageDTO(Garage garage);

    Garage garageDTOtoGarage(GarageDTO garageDTO);

    Set<GarageDTO> toGarageDTOList(Set<Garage> garages);

}

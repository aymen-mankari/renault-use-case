package com.renault.utils;

import com.renault.dto.GarageDTO;
import com.renault.model.Garage;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GarageMapper {
    GarageDTO garageToGarageDTO(Garage garage);
    Garage garageDTOtoGarage(GarageDTO garageDTO);
}

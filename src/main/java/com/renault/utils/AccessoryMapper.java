package com.renault.utils;

import com.renault.dto.AccessoryDTO;
import com.renault.model.Accessory;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AccessoryMapper {
    AccessoryDTO accessoryToAccessoryDTO(Accessory accessory);
    Accessory accessoryDTOtoAccessory(AccessoryDTO accessoryDTO);
}

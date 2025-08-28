package com.renault.utils;

import com.renault.dto.AccessoryDTO;
import com.renault.model.Accessory;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring")
@Component
public interface AccessoryMapper {
    AccessoryDTO accessoryToAccessoryDTO(Accessory accessory);
    Accessory accessoryDTOtoAccessory(AccessoryDTO accessoryDTO);
    Set<AccessoryDTO> toAccessoryDTOList(Set<Accessory> accessorySet);

}

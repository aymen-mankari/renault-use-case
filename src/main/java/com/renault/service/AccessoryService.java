package com.renault.service;

import static com.renault.constants.ApplicationConstants.*;

import com.renault.dto.AccessoryDTO;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Vehicle;
import com.renault.repository.AccessoryRepository;
import com.renault.utils.AccessoryMapper;
import org.mapstruct.factory.Mappers;

public class AccessoryService implements IService<AccessoryDTO> {

    private AccessoryRepository accessoryRepository;
    private AccessoryMapper accessoryMapper;

    public AccessoryService(AccessoryRepository accessoryRepository, AccessoryMapper accessoryMapper) {
        this.accessoryRepository = accessoryRepository;
        this.accessoryMapper = Mappers.getMapper(AccessoryMapper.class);
    }

    @Override
    public AccessoryDTO save(AccessoryDTO obj) {
        try {
            var accessory = this.accessoryMapper.accessoryDTOtoAccessory(obj);
            return this.accessoryMapper.accessoryToAccessoryDTO(this.accessoryRepository.save(accessory));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public AccessoryDTO update(AccessoryDTO obj) {
        var optionalAccessory = this.accessoryRepository.findById(obj.getId());
        if (!optionalAccessory.isPresent())
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY + obj.getId());

        var updatedAccessory = this.accessoryMapper.accessoryDTOtoAccessory(obj);
        try {
            return this.accessoryMapper.accessoryToAccessoryDTO(this.accessoryRepository.save(updatedAccessory));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) {
        var optionalAccessory = this.accessoryRepository.findById(id);
        if (optionalAccessory.isPresent()) {
            var accessory = optionalAccessory.get();
            for(Vehicle vehicle : accessory.getVehicles()){
                vehicle.removeAccessory(accessory);
            }
            this.accessoryRepository.delete(accessory);
            return true;
        } else {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY + id);
        }
    }
}

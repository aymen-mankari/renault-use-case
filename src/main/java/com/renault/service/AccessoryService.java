package com.renault.service;

import static com.renault.constants.ApplicationConstants.*;

import com.renault.dto.AccessoryDTO;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Vehicle;
import com.renault.repository.AccessoryRepository;
import com.renault.repository.VehicleRepository;
import com.renault.utils.AccessoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class AccessoryService implements IService<AccessoryDTO> {

    private AccessoryRepository accessoryRepository;
    private VehicleRepository vehicleRepository;
    private AccessoryMapper accessoryMapper;

    public AccessoryService(AccessoryRepository accessoryRepository, VehicleRepository vehicleRepository, AccessoryMapper accessoryMapper) {
        this.accessoryRepository = accessoryRepository;
        this.vehicleRepository = vehicleRepository;
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
            if(accessory.getVehicles() !=null){
                for (Vehicle vehicle : accessory.getVehicles()) {
                    vehicle.removeAccessory(accessory);
                }
            }
            this.accessoryRepository.delete(accessory);
            return true;
        } else {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY + id);
        }
    }

    public Boolean addAccessoryToVehicle(final Long vehicleId, final Long accessoryId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalAccessory = this.accessoryRepository.findById(accessoryId);
        var vehicle = optionalVehicle.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + vehicleId));
        var accessory = optionalAccessory.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY + accessoryId));
        try {
            vehicle.addAccessory(accessory);
            var addStatus = this.vehicleRepository.save(vehicle);
            if (addStatus != null)
                return true;
            else return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Boolean removeAccessoryFromVehicle(final Long vehicleId, final Long accessoryId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalAccessory = this.accessoryRepository.findById(accessoryId);
        var vehicle = optionalVehicle.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + vehicleId));
        var accessory = optionalAccessory.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY + accessoryId));
        try {
            vehicle.removeAccessory(accessory);
            var removeStatus = this.accessoryRepository.save(accessory);
            if (removeStatus != null)
                return true;
            else return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Set<AccessoryDTO> getListAccessoriesForAVehicle(final Long vehicleId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var vehicle = optionalVehicle.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + vehicleId));
        return this.accessoryMapper.toAccessoryDTOList(vehicle.getAccessories());
    }
}

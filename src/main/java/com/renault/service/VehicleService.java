package com.renault.service;

import com.renault.dto.GarageDTO;
import com.renault.dto.VehicleDTO;
import com.renault.exception.DataNotFoundException;
import com.renault.model.Garage;
import com.renault.repository.GarageRepository;
import com.renault.repository.VehicleRepository;
import com.renault.utils.GarageMapper;
import com.renault.utils.VehicleMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.renault.constants.ApplicationConstants.DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE;
import static com.renault.constants.ApplicationConstants.DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE;

@Service
public class VehicleService implements IService<VehicleDTO> {

    private VehicleRepository vehicleRepository;
    private GarageRepository garageRepository;
    private VehicleMapper vehicleMapper;
    private GarageMapper garageMapper;

    public VehicleService(VehicleRepository vehicleRepository, GarageRepository garageRepository) {
        this.vehicleRepository = vehicleRepository;
        this.garageRepository = garageRepository;
        this.vehicleMapper = Mappers.getMapper(VehicleMapper.class);
        this.garageMapper = Mappers.getMapper(GarageMapper.class);
    }

    @Override
    public VehicleDTO save(VehicleDTO obj) {
        try {
            var vehicle = this.vehicleMapper.vehicleDTOtoVehicle(obj);
            return this.vehicleMapper.vehicleToVehicleDTO(this.vehicleRepository.save(vehicle));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public VehicleDTO update(VehicleDTO obj) {
        var updatedVehicle = this.vehicleMapper.vehicleDTOtoVehicle(obj);
        var existsById = this.vehicleRepository.existsById(updatedVehicle.getId());
        if (!existsById)
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + obj.getId());
        try {
            return this.vehicleMapper.vehicleToVehicleDTO(this.vehicleRepository.save(updatedVehicle));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) {
        var optionalVehicle = this.vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            var vehicle = optionalVehicle.get();
            if (vehicle.getGarages() != null) {
                for (Garage garage : vehicle.getGarages()) {
                    garage.removeVehicle(vehicle);
                }
            }
            this.vehicleRepository.delete(vehicle);
            return true;
        } else {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + id);
        }
    }

    public Boolean addVehicleToGarage(final Long vehicleId, final Long garageId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalGarage = this.garageRepository.findById(garageId);
        if (!optionalGarage.isPresent())
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE + garageId);
        else if (!optionalVehicle.isPresent())
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + vehicleId);

        var vehicle = optionalVehicle.get();
        var garage = optionalGarage.get();
        garage.addVehicle(vehicle);
        try {
            var savedVehicle = this.vehicleRepository.save(vehicle);
            if (savedVehicle != null)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Boolean removeVehicleFromGarage(final Long vehicleId, final Long garageId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalGarage = this.garageRepository.findById(garageId);
        if (!optionalGarage.isPresent())
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE + garageId);
        else if (!optionalVehicle.isPresent())
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE + vehicleId);

        var vehicle = optionalVehicle.get();
        var garage = optionalGarage.get();
        garage.removeVehicle(vehicle);
        try {
            var savedVehicle = this.vehicleRepository.save(vehicle);
            if (savedVehicle != null)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Set<VehicleDTO> getVehiclesRelatedToAGarage(final Long idGarage) {
        var optionalGarage = this.garageRepository.findById(idGarage);
        var garage = optionalGarage.orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE + idGarage));
        return this.vehicleMapper.toVehicleDTOList(garage.getVehicles());
    }

    public Set<GarageDTO> getVehiclesByBrandAssociatedToGarages(final String brand) {
        return this.garageMapper.toGarageDTOList(this.vehicleRepository.findVehiclesByBrandAssociatedToGarages(brand));
    }

}

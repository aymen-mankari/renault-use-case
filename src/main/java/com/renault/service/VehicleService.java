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
import java.util.concurrent.CopyOnWriteArraySet;

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
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        try {
            var vehicle = this.vehicleMapper.vehicleDTOtoVehicle(vehicleDTO);
            return this.vehicleMapper.vehicleToVehicleDTO(this.vehicleRepository.save(vehicle));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        var updatedVehicle = this.vehicleMapper.vehicleDTOtoVehicle(vehicleDTO);
        var existsById = this.vehicleRepository.existsById(updatedVehicle.getId());
        if (!existsById)
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE, vehicleDTO.getId()));
        try {
            var updateResult = this.vehicleRepository.save(updatedVehicle);
            return this.vehicleMapper.vehicleToVehicleDTO(updateResult);
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
                var copyGarages = new CopyOnWriteArraySet<>(vehicle.getGarages());
                for (Garage garage : copyGarages) {
                    vehicle.removeGarage(garage);
                }
            }
            this.vehicleRepository.delete(vehicle);
            return true;
        } else {
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE, id));
        }
    }

    public Boolean addVehicleToGarage(final Long vehicleId, final Long garageId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalGarage = this.garageRepository.findById(garageId);
        if (!optionalGarage.isPresent())
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, garageId));
        else if (!optionalVehicle.isPresent())
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE, vehicleId));

        var vehicle = optionalVehicle.get();
        var garage = optionalGarage.get();
        vehicle.addGarage(garage);
        try {
            var updateStatus = this.vehicleRepository.save(vehicle);
            return updateStatus != null ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Boolean removeVehicleFromGarage(final Long vehicleId, final Long garageId) {
        var optionalVehicle = this.vehicleRepository.findById(vehicleId);
        var optionalGarage = this.garageRepository.findById(garageId);
        if (!optionalGarage.isPresent())
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, garageId));
        else if (!optionalVehicle.isPresent())
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE, vehicleId));

        var vehicle = optionalVehicle.get();
        var garage = optionalGarage.get();
        vehicle.removeGarage(garage);
        try {
            var updateStatus = this.vehicleRepository.save(vehicle);
            return updateStatus != null ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Set<VehicleDTO> getVehiclesRelatedToAGarage(final Long idGarage) {
        var optionalGarage = this.garageRepository.findById(idGarage);
        var garage = optionalGarage.orElseThrow(() -> new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, idGarage)));
        return this.vehicleMapper.toVehicleDTOList(garage.getVehicles());
    }

    public Set<GarageDTO> getVehiclesByBrandAssociatedToGarages(final String brand) {
        return this.garageMapper.toGarageDTOList(this.vehicleRepository.findVehiclesByBrandAssociatedToGarages(brand));
    }

}

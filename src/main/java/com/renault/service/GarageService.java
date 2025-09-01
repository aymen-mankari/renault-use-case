package com.renault.service;

import com.renault.dto.GarageDTO;
import com.renault.enums.TypeVehicle;
import com.renault.exception.DataNotFoundException;
import com.renault.repository.GarageRepository;
import com.renault.utils.GarageMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.renault.constants.ApplicationConstants.DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE;

@Service
@Slf4j
public class GarageService implements IService<GarageDTO> {
    private GarageRepository garageRepository;
    private GarageMapper garageMapper;

    public GarageService(GarageRepository garageRepository, GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = Mappers.getMapper(GarageMapper.class);
    }

    @Override
    public GarageDTO save(GarageDTO garageDTO) {
        try {
            var garage = garageMapper.garageDTOtoGarage(garageDTO);
            return garageMapper.garageToGarageDTO(this.garageRepository.save(garage));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public GarageDTO update(GarageDTO garageDTO) {
        var updatedGarage = garageMapper.garageDTOtoGarage(garageDTO);
        var existsById = this.garageRepository.existsById(updatedGarage.getId());
        if (!existsById)
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, garageDTO.getId()));
        try {
            return garageMapper.garageToGarageDTO(this.garageRepository.save(updatedGarage));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) {
        var garage = this.garageRepository.findById(id);
        if (garage.isPresent()) {
            this.garageRepository.deleteById(id);
            return true;
        } else {
            throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, id));
        }
    }

    public GarageDTO findById(final Long id) {
        var optGarage = this.garageRepository.findById(id);
        if (optGarage.isPresent()) {
            var garageDTO = garageMapper.garageToGarageDTO(optGarage.get());
            return garageDTO;
        } else throw new DataNotFoundException(String.format(DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE, id));
    }

    public Set<GarageDTO> findByTypeVehicles(final String typeVehicle) {
        var garages = this.garageRepository.findByTypeVehicles(TypeVehicle.valueOf(typeVehicle));
        return this.garageMapper.toGarageDTOList(garages);
    }

    public Set<GarageDTO> findByAccessory(final String accessoryName) {
        var garages = this.garageRepository.findByAccessoryName(accessoryName);
        return this.garageMapper.toGarageDTOList(garages);
    }

}

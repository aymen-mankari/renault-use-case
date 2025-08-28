package com.renault.dto;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class VehicleDTO {
    private Long id;
    private String brand;
    private TypeVehicle typeVehicle;
    private LocalDate manufactureYear;
    private FuelType fuelType;
    private Set<AccessoryDTO> accessories = new HashSet<>();

    //commented this out to avoid cyclic reference problem during mapping bidirectional association
    //private Set<GarageDTO> garages = new HashSet<>();
}

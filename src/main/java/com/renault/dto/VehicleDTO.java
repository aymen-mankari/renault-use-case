package com.renault.dto;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleDTO {
    private Long id;
    private String brand;
    private TypeVehicle typeVehicle;
    private Integer manufactureYear;
    private FuelType fuelType;
    private Set<AccessoryDTO> accessories = new HashSet<>();

    //commented this out to avoid cyclic reference problem during mapping bidirectional association
    //private Set<GarageDTO> garages = new HashSet<>();


    public VehicleDTO(Long id, String brand, TypeVehicle typeVehicle, Integer manufactureYear, FuelType fuelType) {
        this.id = id;
        this.brand = brand;
        this.typeVehicle = typeVehicle;
        this.manufactureYear = manufactureYear;
        this.fuelType = fuelType;
    }

    public VehicleDTO(String brand, TypeVehicle typeVehicle, Integer manufactureYear, FuelType fuelType) {
        this.brand = brand;
        this.typeVehicle = typeVehicle;
        this.manufactureYear = manufactureYear;
        this.fuelType = fuelType;
        this.accessories = accessories;
    }
}

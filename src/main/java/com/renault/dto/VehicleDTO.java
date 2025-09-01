package com.renault.dto;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import lombok.*;

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
}

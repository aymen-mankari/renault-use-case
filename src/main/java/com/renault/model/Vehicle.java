package com.renault.model;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import com.renault.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.renault.constants.ApplicationConstants.BAD_REQUEST_EXCEPTION_ACCESSORY_ALREADY_FOUND_IN_ACCESSORY_LIST;
import static com.renault.constants.ApplicationConstants.BAD_REQUEST_EXCEPTION_ACCESSORY_NOT_FOUND_IN_ACCESSORY_LIST;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private String brand;
    private Integer manufactureYear;
    @Enumerated(EnumType.STRING)
    private TypeVehicle typeVehicle;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @ManyToMany(mappedBy = "vehicles")
    private Set<Garage> garages = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "vehicle_accessory",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id"))
    private Set<Accessory> accessories = new HashSet<>();

    public void addGarage(Garage garage) {
        garage.addVehicle(this);
    }

    public void removeGarage(Garage garage) {
        garage.removeVehicle(this);
    }

    public void addAccessory(Accessory accessory) {
        if (this.accessories.contains(accessory))
            throw new BadRequestException(String.format(BAD_REQUEST_EXCEPTION_ACCESSORY_ALREADY_FOUND_IN_ACCESSORY_LIST, accessory.getId()));
        this.accessories.add(accessory);
        accessory.getVehicles().add(this);
    }

    public void removeAccessory(Accessory accessory) {
        if (!this.accessories.contains(accessory))
            throw new BadRequestException(String.format(BAD_REQUEST_EXCEPTION_ACCESSORY_NOT_FOUND_IN_ACCESSORY_LIST, accessory.getId()));
        this.accessories.remove(accessory);
        accessory.getVehicles().remove(this);
    }
}

package com.renault.model;

import com.renault.enums.FuelType;
import com.renault.enums.TypeVehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void addGarage(Garage garage){
        this.garages.add(garage);
    }

    public void removeGarage(Garage garage){
        this.garages.remove(garage);
    }

    public void addAccessory(Accessory accessory){
        this.accessories.add(accessory);
        accessory.addVehicle(this);
    }

    public void removeAccessory(Accessory accessory){
        this.accessories.remove(accessory);
        accessory.removeVehicle(this);
    }
}

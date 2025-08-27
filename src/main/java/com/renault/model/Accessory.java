package com.renault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Accessory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
    @ManyToMany(mappedBy = "accessories")
    private Set<Vehicle> vehicles = new HashSet<>();

    public void addVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }
}

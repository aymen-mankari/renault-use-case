package com.renault.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Accessory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
    @ManyToMany(mappedBy = "accessories")
    private Set<Vehicle> vehicles = new HashSet<>();

    public void addVehicle(Vehicle vehicle){
        vehicle.addAccessory(this);
    }

    public void removeVehicle(Vehicle vehicle){
        vehicle.removeAccessory(this);
    }
}

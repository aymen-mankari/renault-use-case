package com.renault.model;

import static com.renault.constants.ApplicationConstants.*;
import com.renault.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    @ElementCollection
    @CollectionTable(
            name = "schedule_time_slots",
            joinColumns = @JoinColumn(name = "schedule_garage_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, OpeningTime> timeSlots = new EnumMap<>(DayOfWeek.class);

    @ManyToMany
    @JoinTable(
            name = "garage_vehicle",
            joinColumns = @JoinColumn(name = "garage_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles = new HashSet<>();


    public void addTimeSlot(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        var openingTime = new OpeningTime();
        openingTime.setStartTime(startTime);
        openingTime.setEndTime(endTime);
        this.timeSlots.put(day, openingTime);
    }

    public void removeTimeSlot(DayOfWeek day) {
        this.timeSlots.remove(day);
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.getVehicles().size() > 5) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION_GARAGE_SIZE_REACHED);
        }
        this.vehicles.add(vehicle);
        vehicle.addGarage(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        if(!vehicles.contains(vehicle))
            throw new BadRequestException(String.format(BAD_REQUEST_EXCEPTION_VEHICLE_NOT_FOUND_IN_GARAGE, vehicle.getId(),this.getId()));
        this.vehicles.remove(vehicle);
        vehicle.removeGarage(this);
    }
}



package com.renault.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
public class GarageDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Map<DayOfWeek, OpeningTimeDTO> timeSlots = new EnumMap<>(DayOfWeek.class);
    private Set<VehicleDTO> vehicles = new HashSet<>();
}



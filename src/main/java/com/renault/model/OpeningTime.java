package com.renault.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalTime;

@Data
@Embeddable
public class OpeningTime {
    private LocalTime startTime;
    private LocalTime endTime;
}

package com.renault.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class OpeningTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}

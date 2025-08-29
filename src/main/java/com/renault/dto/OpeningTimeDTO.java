package com.renault.dto;

import lombok.*;

import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OpeningTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}

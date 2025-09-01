package com.renault.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessoryDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
}

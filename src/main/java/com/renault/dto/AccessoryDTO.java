package com.renault.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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

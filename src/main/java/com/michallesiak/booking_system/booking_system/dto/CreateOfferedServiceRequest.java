package com.michallesiak.booking_system.booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferedServiceRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
}
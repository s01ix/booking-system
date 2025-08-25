package com.michallesiak.booking_system.booking_system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistDto {
    private Long id;
    private Long userId;
    private String specialization;
    private Long workAddressId;
    private boolean acceptingAppointments;
}
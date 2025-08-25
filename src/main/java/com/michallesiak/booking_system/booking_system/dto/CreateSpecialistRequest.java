package com.michallesiak.booking_system.booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpecialistRequest {
    private Long userId;
    private String specialization;
    private Long workAddressId;
}
package com.michallesiak.booking_system.booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAvailableSlotRequest {
    private Long specialistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}


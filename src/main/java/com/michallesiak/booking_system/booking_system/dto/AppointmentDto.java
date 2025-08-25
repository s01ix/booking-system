package com.michallesiak.booking_system.booking_system.dto;

import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long id;
    private Long userId;
    private Long slotId;
    private Long serviceId;
    private AppointmentStatus status;
    private String notes;
}

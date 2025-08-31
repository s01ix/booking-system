package com.michallesiak.booking_system.booking_system.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentNotesRequest {

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
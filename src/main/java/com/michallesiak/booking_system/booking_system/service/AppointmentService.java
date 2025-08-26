package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.AppointmentDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAppointmentRequest;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(CreateAppointmentRequest request);
    AppointmentDto getAppointmentById(Long id);
    List<AppointmentDto> getAllAppointments();
    List<AppointmentDto> getAppointmentsByUserId(Long userId);
    List<AppointmentDto> getAppointmentsByStatus(AppointmentStatus status);
    AppointmentDto updateAppointmentStatus(Long id, AppointmentStatus status);
    AppointmentDto updateAppointmentNotes(Long id, String notes);
    void cancelAppointment(Long id);
    void deleteAppointment(Long id);
    boolean isSlotAvailable(Long slotId);
}
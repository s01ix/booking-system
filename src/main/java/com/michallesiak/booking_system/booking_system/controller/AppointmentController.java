package com.michallesiak.booking_system.booking_system.controller;

import com.michallesiak.booking_system.booking_system.dto.AppointmentDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAppointmentRequest;
import com.michallesiak.booking_system.booking_system.dto.UpdateAppointmentNotesRequest;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import com.michallesiak.booking_system.booking_system.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        log.info("REST request to create appointment for user: {}, slot: {}, service: {}",
                request.getUserId(), request.getSlotId(), request.getServiceId());
        AppointmentDto createdAppointment = appointmentService.createAppointment(request);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        log.info("REST request to get appointment with id: {}", id);

        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments(
            @RequestParam(required = false) AppointmentStatus status) {
        log.info("REST request to get all appointments with status filter: {}", status);
        List<AppointmentDto> appointments = status != null ?
                appointmentService.getAppointmentsByStatus(status) :
                appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) AppointmentStatus status) {
        log.info("REST request to get appointments for user: {} with status: {}", userId, status);

        List<AppointmentDto> appointments;
        if (status != null) {
            // Możesz dodać metodę getAppointmentsByUserIdAndStatus() do service
            appointments = appointmentService.getAppointmentsByUserId(userId).stream()
                    .filter(apt -> apt.getStatus() == status)
                    .toList();
        } else {
            appointments = appointmentService.getAppointmentsByUserId(userId);
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        log.info("REST request to get appointments with status: {}", status);

        List<AppointmentDto> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        log.info("REST request to update appointment: {} status to: {}", id, status);

        AppointmentDto updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/{id}/notes")
    public ResponseEntity<AppointmentDto> updateAppointmentNotes(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentNotesRequest request) {
        log.info("REST request to update notes for appointment: {}", id);

        AppointmentDto updatedAppointment = appointmentService.updateAppointmentNotes(id, request.getNotes());
        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AppointmentDto> confirmAppointment(@PathVariable Long id) {
        log.info("REST request to confirm appointment: {}", id);

        AppointmentDto confirmedAppointment = appointmentService.updateAppointmentStatus(id, AppointmentStatus.CONFIRMED);
        return ResponseEntity.ok(confirmedAppointment);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDto> completeAppointment(@PathVariable Long id) {
        log.info("REST request to complete appointment: {}", id);

        AppointmentDto completedAppointment = appointmentService.updateAppointmentStatus(id, AppointmentStatus.COMPLETED);
        return ResponseEntity.ok(completedAppointment);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        log.info("REST request to cancel appointment: {}", id);

        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.info("REST request to delete appointment with id: {}", id);

        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/slot/{slotId}/check")
    public ResponseEntity<Boolean> checkSlotAvailability(@PathVariable Long slotId) {
        log.info("REST request to check slot availability: {}", slotId);

        boolean isAvailable = appointmentService.isSlotAvailable(slotId);
        return ResponseEntity.ok(isAvailable);
    }
}
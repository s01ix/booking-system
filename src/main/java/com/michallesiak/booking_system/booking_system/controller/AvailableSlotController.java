package com.michallesiak.booking_system.booking_system.controller;

import com.michallesiak.booking_system.booking_system.dto.AvailableSlotDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAvailableSlotRequest;
import com.michallesiak.booking_system.booking_system.dto.CreateMultipleSlotsRequest;
import com.michallesiak.booking_system.booking_system.service.AvailableSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Slf4j
public class AvailableSlotController {

    private final AvailableSlotService availableSlotService;

    @PostMapping
    public ResponseEntity<AvailableSlotDto> createSlot(@Valid @RequestBody CreateAvailableSlotRequest request) {
        log.info("REST request to create slot for specialist: {} from {} to {}",
                request.getSpecialistId(), request.getStartTime(), request.getEndTime());

        AvailableSlotDto createdSlot = availableSlotService.createSlot(request);
        return new ResponseEntity<>(createdSlot, HttpStatus.CREATED);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<AvailableSlotDto>> createMultipleSlots(@Valid @RequestBody CreateMultipleSlotsRequest request) {
        log.info("REST request to create multiple slots for specialist: {} from {} to {} with duration: {} minutes",
                request.getSpecialistId(), request.getStartTime(), request.getEndTime(), request.getDurationMinutes());

        List<AvailableSlotDto> createdSlots = availableSlotService.createMultipleSlots(
                request.getSpecialistId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDurationMinutes()
        );
        return new ResponseEntity<>(createdSlots, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailableSlotDto> getSlotById(@PathVariable Long id) {
        log.info("REST request to get slot with id: {}", id);

        AvailableSlotDto slot = availableSlotService.getSlotById(id);
        return ResponseEntity.ok(slot);
    }

    @GetMapping
    public ResponseEntity<List<AvailableSlotDto>> getAllSlots() {
        log.info("REST request to get all slots");

        List<AvailableSlotDto> slots = availableSlotService.getAllSlots();
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<AvailableSlotDto>> getSlotsBySpecialist(
            @PathVariable Long specialistId,
            @RequestParam(defaultValue = "false") boolean availableOnly) {
        log.info("REST request to get slots for specialist: {}, availableOnly: {}", specialistId, availableOnly);

        List<AvailableSlotDto> slots = availableOnly ?
                availableSlotService.getAvailableSlotsBySpecialistId(specialistId) :
                availableSlotService.getSlotsBySpecialistId(specialistId);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/specialist/{specialistId}/available")
    public ResponseEntity<List<AvailableSlotDto>> getAvailableSlotsBySpecialist(@PathVariable Long specialistId) {
        log.info("REST request to get available slots for specialist: {}", specialistId);

        List<AvailableSlotDto> slots = availableSlotService.getAvailableSlotsBySpecialistId(specialistId);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/specialist/{specialistId}/date/{date}")
    public ResponseEntity<List<AvailableSlotDto>> getAvailableSlotsBySpecialistAndDate(
            @PathVariable Long specialistId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST request to get available slots for specialist: {} on date: {}", specialistId, date);

        List<AvailableSlotDto> slots = availableSlotService.getAvailableSlotsBySpecialistAndDate(specialistId, date);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/specialist/{specialistId}/range")
    public ResponseEntity<List<AvailableSlotDto>> getSlotsBySpecialistAndDateRange(
            @PathVariable Long specialistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("REST request to get slots for specialist: {} between {} and {}", specialistId, start, end);

        List<AvailableSlotDto> slots = availableSlotService.getSlotsBySpecialistAndDateRange(specialistId, start, end);
        return ResponseEntity.ok(slots);
    }

    @PatchMapping("/{id}/book")
    public ResponseEntity<AvailableSlotDto> markSlotAsBooked(@PathVariable Long id) {
        log.info("REST request to mark slot as booked: {}", id);

        AvailableSlotDto updatedSlot = availableSlotService.markSlotAsBooked(id);
        return ResponseEntity.ok(updatedSlot);
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<AvailableSlotDto> markSlotAsAvailable(@PathVariable Long id) {
        log.info("REST request to mark slot as available: {}", id);

        AvailableSlotDto updatedSlot = availableSlotService.markSlotAsAvailable(id);
        return ResponseEntity.ok(updatedSlot);
    }

    @GetMapping("/{id}/check-availability")
    public ResponseEntity<Boolean> checkSlotAvailability(@PathVariable Long id) {
        log.info("REST request to check availability for slot: {}", id);

        boolean isAvailable = availableSlotService.isSlotAvailable(id);
        return ResponseEntity.ok(isAvailable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        log.info("REST request to delete slot with id: {}", id);

        availableSlotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/specialist/{specialistId}/range")
    public ResponseEntity<Void> deleteSlotsBySpecialistAndDateRange(
            @PathVariable Long specialistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("REST request to delete slots for specialist: {} between {} and {}", specialistId, start, end);

        availableSlotService.deleteSlotsBySpecialistAndDateRange(specialistId, start, end);
        return ResponseEntity.noContent().build();
    }
}
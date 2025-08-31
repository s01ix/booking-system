package com.michallesiak.booking_system.booking_system.controller;

import com.michallesiak.booking_system.booking_system.dto.CreateSpecialistRequest;
import com.michallesiak.booking_system.booking_system.dto.SpecialistDto;
import com.michallesiak.booking_system.booking_system.service.SpecialistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialists")
@RequiredArgsConstructor
@Slf4j
public class SpecialistController {

    private final SpecialistService specialistService;

    @PostMapping
    public ResponseEntity<SpecialistDto> createSpecialist(@Valid @RequestBody CreateSpecialistRequest request) {
        log.info("REST request to create specialist for user: {} with specialization: {}",
                request.getUserId(), request.getSpecialization());

        SpecialistDto createdSpecialist = specialistService.createSpecialist(request);
        return new ResponseEntity<>(createdSpecialist, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialistDto> getSpecialistById(@PathVariable Long id) {
        log.info("REST request to get specialist with id: {}", id);

        SpecialistDto specialist = specialistService.getSpecialistById(id);
        return ResponseEntity.ok(specialist);
    }

    @GetMapping
    public ResponseEntity<List<SpecialistDto>> getAllSpecialists(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        log.info("REST request to get all specialists, activeOnly: {}", activeOnly);

        List<SpecialistDto> specialists = activeOnly ?
                specialistService.getActiveSpecialists() :
                specialistService.getAllSpecialists();
        return ResponseEntity.ok(specialists);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SpecialistDto>> getActiveSpecialists() {
        log.info("REST request to get active specialists");

        List<SpecialistDto> specialists = specialistService.getActiveSpecialists();
        return ResponseEntity.ok(specialists);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SpecialistDto> getSpecialistByUserId(@PathVariable Long userId) {
        log.info("REST request to get specialist by user id: {}", userId);

        SpecialistDto specialist = specialistService.getSpecialistByUserId(userId);
        return ResponseEntity.ok(specialist);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpecialistDto>> searchSpecialists(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long serviceId) {
        log.info("REST request to search specialists - specialization: {}, city: {}, serviceId: {}",
                specialization, city, serviceId);

        List<SpecialistDto> specialists;

        if (specialization != null && !specialization.trim().isEmpty()) {
            specialists = specialistService.getSpecialistsBySpecialization(specialization);
        } else if (city != null && !city.trim().isEmpty()) {
            specialists = specialistService.getSpecialistsByCity(city);
        } else if (serviceId != null) {
            specialists = specialistService.getSpecialistsByServiceId(serviceId);
        } else {
            specialists = specialistService.getActiveSpecialists();
        }

        return ResponseEntity.ok(specialists);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<SpecialistDto>> getSpecialistsBySpecialization(@PathVariable String specialization) {
        log.info("REST request to get specialists by specialization: {}", specialization);

        List<SpecialistDto> specialists = specialistService.getSpecialistsBySpecialization(specialization);
        return ResponseEntity.ok(specialists);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<SpecialistDto>> getSpecialistsByCity(@PathVariable String city) {
        log.info("REST request to get specialists in city: {}", city);

        List<SpecialistDto> specialists = specialistService.getSpecialistsByCity(city);
        return ResponseEntity.ok(specialists);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<SpecialistDto>> getSpecialistsByService(@PathVariable Long serviceId) {
        log.info("REST request to get specialists offering service: {}", serviceId);

        List<SpecialistDto> specialists = specialistService.getSpecialistsByServiceId(serviceId);
        return ResponseEntity.ok(specialists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialistDto> updateSpecialist(@PathVariable Long id,
                                                          @Valid @RequestBody CreateSpecialistRequest request) {
        log.info("REST request to update specialist with id: {}", id);

        SpecialistDto updatedSpecialist = specialistService.updateSpecialist(id, request);
        return ResponseEntity.ok(updatedSpecialist);
    }

    @PatchMapping("/{id}/specialization")
    public ResponseEntity<SpecialistDto> updateSpecialization(@PathVariable Long id,
                                                              @RequestParam String specialization) {
        log.info("REST request to update specialization for specialist: {} to: {}", id, specialization);

        SpecialistDto updatedSpecialist = specialistService.updateSpecialization(id, specialization);
        return ResponseEntity.ok(updatedSpecialist);
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<SpecialistDto> updateWorkAddress(@PathVariable Long id,
                                                           @RequestParam(required = false) Long addressId) {
        log.info("REST request to update work address for specialist: {} to address: {}", id, addressId);

        SpecialistDto updatedSpecialist = specialistService.updateWorkAddress(id, addressId);
        return ResponseEntity.ok(updatedSpecialist);
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<SpecialistDto> enableAppointments(@PathVariable Long id) {
        log.info("REST request to enable appointments for specialist: {}", id);

        SpecialistDto updatedSpecialist = specialistService.enableAppointments(id);
        return ResponseEntity.ok(updatedSpecialist);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<SpecialistDto> disableAppointments(@PathVariable Long id) {
        log.info("REST request to disable appointments for specialist: {}", id);

        SpecialistDto updatedSpecialist = specialistService.disableAppointments(id);
        return ResponseEntity.ok(updatedSpecialist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialist(@PathVariable Long id) {
        log.info("REST request to delete specialist with id: {}", id);

        specialistService.deleteSpecialist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-user/{userId}")
    public ResponseEntity<Boolean> checkIfUserIsSpecialist(@PathVariable Long userId) {
        log.info("REST request to check if user: {} is already a specialist", userId);

        boolean isSpecialist = specialistService.isUserAlreadySpecialist(userId);
        return ResponseEntity.ok(isSpecialist);
    }
}
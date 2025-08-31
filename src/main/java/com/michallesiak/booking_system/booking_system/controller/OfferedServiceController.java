package com.michallesiak.booking_system.booking_system.controller;

import com.michallesiak.booking_system.booking_system.dto.CreateOfferedServiceRequest;
import com.michallesiak.booking_system.booking_system.dto.OfferedServiceDto;
import com.michallesiak.booking_system.booking_system.service.OfferedServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Slf4j
public class OfferedServiceController {

    private final OfferedServiceService offeredServiceService;

    @PostMapping
    public ResponseEntity<OfferedServiceDto> createService(@Valid @RequestBody CreateOfferedServiceRequest request) {
        log.info("REST request to create service: {}", request.getName());

        OfferedServiceDto createdService = offeredServiceService.createService(request);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferedServiceDto> getServiceById(@PathVariable Long id) {
        log.info("REST request to get service with id: {}", id);

        OfferedServiceDto service = offeredServiceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping
    public ResponseEntity<List<OfferedServiceDto>> getAllServices(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        log.info("REST request to get all services, activeOnly: {}", activeOnly);

        List<OfferedServiceDto> services = activeOnly ?
                offeredServiceService.getActiveServices() :
                offeredServiceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OfferedServiceDto>> getActiveServices() {
        log.info("REST request to get active services");

        List<OfferedServiceDto> services = offeredServiceService.getActiveServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<OfferedServiceDto>> getServicesBySpecialist(@PathVariable Long specialistId) {
        log.info("REST request to get services for specialist: {}", specialistId);

        List<OfferedServiceDto> services = offeredServiceService.getServicesBySpecialistId(specialistId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OfferedServiceDto>> searchServices(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer duration) {
        log.info("REST request to search services with filters - minPrice: {}, maxPrice: {}, duration: {}",
                minPrice, maxPrice, duration);

        List<OfferedServiceDto> services;

        if (minPrice != null && maxPrice != null) {
            services = offeredServiceService.getServicesByPriceRange(minPrice, maxPrice);
        } else if (duration != null) {
            services = offeredServiceService.getServicesByDuration(duration);
        } else {
            services = offeredServiceService.getActiveServices();
        }

        return ResponseEntity.ok(services);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferedServiceDto> updateService(@PathVariable Long id,
                                                           @Valid @RequestBody CreateOfferedServiceRequest request) {
        log.info("REST request to update service with id: {}", id);

        OfferedServiceDto updatedService = offeredServiceService.updateService(id, request);
        return ResponseEntity.ok(updatedService);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<OfferedServiceDto> updateServicePrice(@PathVariable Long id,
                                                                @RequestBody BigDecimal newPrice) {
        log.info("REST request to update price for service: {} to: {}", id, newPrice);

        OfferedServiceDto updatedService = offeredServiceService.updateServicePrice(id, newPrice);
        return ResponseEntity.ok(updatedService);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<OfferedServiceDto> activateService(@PathVariable Long id) {
        log.info("REST request to activate service: {}", id);

        OfferedServiceDto activatedService = offeredServiceService.activateService(id);
        return ResponseEntity.ok(activatedService);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<OfferedServiceDto> deactivateService(@PathVariable Long id) {
        log.info("REST request to deactivate service: {}", id);

        OfferedServiceDto deactivatedService = offeredServiceService.deactivateService(id);
        return ResponseEntity.ok(deactivatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.info("REST request to delete service with id: {}", id);

        offeredServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkServiceExists(@RequestParam String name) {
        log.info("REST request to check if service exists with name: {}", name);

        boolean exists = offeredServiceService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}
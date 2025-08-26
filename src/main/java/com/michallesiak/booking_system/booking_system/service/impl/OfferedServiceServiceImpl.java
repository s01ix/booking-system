package com.michallesiak.booking_system.booking_system.service.impl;

import com.michallesiak.booking_system.booking_system.dto.CreateOfferedServiceRequest;
import com.michallesiak.booking_system.booking_system.dto.OfferedServiceDto;
import com.michallesiak.booking_system.booking_system.exception.ServiceAlreadyExistsException;
import com.michallesiak.booking_system.booking_system.exception.ServiceNotFoundException;
import com.michallesiak.booking_system.booking_system.mapper.OfferedServiceMapper;
import com.michallesiak.booking_system.booking_system.model.OfferedService;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import com.michallesiak.booking_system.booking_system.repository.AppointmentRepository;
import com.michallesiak.booking_system.booking_system.repository.OfferedServiceRepository;
import com.michallesiak.booking_system.booking_system.repository.SpecialistRepository;
import com.michallesiak.booking_system.booking_system.service.OfferedServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OfferedServiceServiceImpl implements OfferedServiceService {

    private final OfferedServiceRepository offeredServiceRepository;
    private final SpecialistRepository specialistRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public OfferedServiceDto createService(CreateOfferedServiceRequest request) {
        log.info("Creating service with name: {}", request.getName());
        validateServiceRequest(request);
        if (offeredServiceRepository.existsByName(request.getName())) {
            throw new ServiceAlreadyExistsException("Service with name '" + request.getName() + "' already exists");
        }

        OfferedService service = OfferedServiceMapper.toEntity(request);
        OfferedService savedService = offeredServiceRepository.save(service);
        log.info("Service created successfully with id: {}", savedService.getId());
        return OfferedServiceMapper.toDto(savedService);
    }

    @Override
    public OfferedServiceDto getServiceById(Long id) {
        log.info("Fetching service with id: {}", id);
        OfferedService service = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));

        return OfferedServiceMapper.toDto(service);
    }

    @Override
    public List<OfferedServiceDto> getAllServices() {
        log.info("Fetching all services");
        List<OfferedService> services = offeredServiceRepository.findAll();
        return services.stream().map(OfferedServiceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OfferedServiceDto> getActiveServices() {
        log.info("Fetching active services");
        List<OfferedService> services = offeredServiceRepository.findByActiveTrue();
        return services.stream().map(OfferedServiceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OfferedServiceDto> getServicesBySpecialistId(Long specialistId) {
        log.info("Fetching services for specialist: {}", specialistId);
        if (!specialistRepository.existsById(specialistId)) {
            throw new EntityNotFoundException("Specialist not found with id: " + specialistId);
        }
        List<OfferedService> services = offeredServiceRepository.findBySpecialistsIdAndActiveTrue(specialistId);
        return services.stream().map(OfferedServiceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OfferedServiceDto updateService(Long id, CreateOfferedServiceRequest request) {
        log.info("Updating service with id: {}", id);
        validateServiceRequest(request);
        OfferedService existingService = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
        if (!existingService.getName().equals(request.getName()) &&
                offeredServiceRepository.existsByName(request.getName())) {
            throw new ServiceAlreadyExistsException("Service with name '" + request.getName() + "' already exists");
        }
        existingService.setName(request.getName());
        existingService.setDescription(request.getDescription());
        existingService.setPrice(request.getPrice());
        existingService.setDurationMinutes(request.getDurationMinutes());
        OfferedService updatedService = offeredServiceRepository.save(existingService);
        log.info("Service updated successfully with id: {}", updatedService.getId());
        return OfferedServiceMapper.toDto(updatedService);
    }

    @Override
    @Transactional
    public OfferedServiceDto updateServicePrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for service: {} to: {}", id, newPrice);
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0");
        }
        OfferedService service = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
        BigDecimal oldPrice = service.getPrice();
        service.setPrice(newPrice);
        OfferedService updatedService = offeredServiceRepository.save(service);
        log.info("Service price updated from {} to {} for id: {}", oldPrice, newPrice, id);
        return OfferedServiceMapper.toDto(updatedService);
    }

    @Override
    @Transactional
    public OfferedServiceDto activateService(Long id) {
        log.info("Activating service with id: {}", id);
        OfferedService service = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
        if (service.isActive()) {
            throw new IllegalStateException("Service is already active");
        }
        service.setActive(true);
        OfferedService updatedService = offeredServiceRepository.save(service);
        log.info("Service activated successfully: {}", id);
        return OfferedServiceMapper.toDto(updatedService);
    }

    @Override
    @Transactional
    public OfferedServiceDto deactivateService(Long id) {
        log.info("Deactivating service with id: {}", id);
        OfferedService service = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
        if (!service.isActive()) {
            throw new IllegalStateException("Service is already inactive");
        }
        service.setActive(false);
        OfferedService updatedService = offeredServiceRepository.save(service);
        log.info("Service deactivated successfully: {}", id);
        return OfferedServiceMapper.toDto(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        log.info("Deleting service with id: {}", id);
        OfferedService service = offeredServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
        boolean hasActiveAppointments = appointmentRepository.existsByServiceIdAndStatusIn(
                id, List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED));
        if (hasActiveAppointments) {
            throw new IllegalStateException("Cannot delete service with active appointments. Deactivate service instead.");
        }
        offeredServiceRepository.deleteById(id);
        log.info("Service deleted successfully: {}", id);
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("Checking if service exists with name: {}", name);
        return offeredServiceRepository.existsByName(name);
    }

    @Override
    public List<OfferedServiceDto> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching services with price between {} and {}", minPrice, maxPrice);
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        List<OfferedService> services = offeredServiceRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice);
        return services.stream().map(OfferedServiceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OfferedServiceDto> getServicesByDuration(Integer durationMinutes) {
        log.info("Fetching services with duration: {} minutes", durationMinutes);
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        List<OfferedService> services = offeredServiceRepository.findByDurationMinutesAndActiveTrue(durationMinutes);
        return services.stream().map(OfferedServiceMapper::toDto).collect(Collectors.toList());
    }

    private void validateServiceRequest(CreateOfferedServiceRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be empty");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0");
        }
        if (request.getDurationMinutes() == null || request.getDurationMinutes() < 15) {
            throw new IllegalArgumentException("Duration must be at least 15 minutes");
        }
    }
}
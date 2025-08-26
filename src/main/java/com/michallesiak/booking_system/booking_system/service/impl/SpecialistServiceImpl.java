package com.michallesiak.booking_system.booking_system.service.impl;

import com.michallesiak.booking_system.booking_system.dto.CreateSpecialistRequest;
import com.michallesiak.booking_system.booking_system.dto.SpecialistDto;
import com.michallesiak.booking_system.booking_system.exception.SpecialistAlreadyExistsException;
import com.michallesiak.booking_system.booking_system.exception.SpecialistNotFoundException;
import com.michallesiak.booking_system.booking_system.mapper.SpecialistMapper;
import com.michallesiak.booking_system.booking_system.model.Address;
import com.michallesiak.booking_system.booking_system.model.Specialist;
import com.michallesiak.booking_system.booking_system.model.User;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import com.michallesiak.booking_system.booking_system.repository.AddressRepository;
import com.michallesiak.booking_system.booking_system.repository.AppointmentRepository;
import com.michallesiak.booking_system.booking_system.repository.SpecialistRepository;
import com.michallesiak.booking_system.booking_system.repository.UserRepository;
import com.michallesiak.booking_system.booking_system.service.SpecialistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SpecialistServiceImpl implements SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public SpecialistDto createSpecialist(CreateSpecialistRequest request) {
        log.info("Creating specialist for user: {} with specialization: {}",
                request.getUserId(), request.getSpecialization());
        validateSpecialistRequest(request);
        if (isUserAlreadySpecialist(request.getUserId())) {
            throw new SpecialistAlreadyExistsException("User with id " + request.getUserId() + " is already a specialist");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
        Address workAddress = null;
        if (request.getWorkAddressId() != null) {
            workAddress = addressRepository.findById(request.getWorkAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + request.getWorkAddressId()));
        }
        Specialist specialist = SpecialistMapper.toEntity(request, user, workAddress);
        Specialist savedSpecialist = specialistRepository.save(specialist);
        log.info("Specialist created successfully with id: {}", savedSpecialist.getId());
        return SpecialistMapper.toDto(savedSpecialist);
    }

    @Override
    public SpecialistDto getSpecialistById(Long id) {
        log.info("Fetching specialist with id: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        return SpecialistMapper.toDto(specialist);
    }

    @Override
    public List<SpecialistDto> getAllSpecialists() {
        log.info("Fetching all specialists");
        List<Specialist> specialists = specialistRepository.findAll();
        return specialists.stream().map(SpecialistMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SpecialistDto> getActiveSpecialists() {
        log.info("Fetching active specialists");
        List<Specialist> specialists = specialistRepository.findByAcceptingAppointmentsTrue();
        return specialists.stream().map(SpecialistMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SpecialistDto> getSpecialistsBySpecialization(String specialization) {
        log.info("Fetching specialists by specialization: {}", specialization);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }

        List<Specialist> specialists = specialistRepository.findBySpecializationContainingIgnoreCase(specialization);
        return specialists.stream().map(SpecialistMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SpecialistDto> getSpecialistsByServiceId(Long serviceId) {
        log.info("Fetching specialists offering service: {}", serviceId);
        List<Specialist> specialists = specialistRepository.findByServicesIdAndAcceptingAppointmentsTrue(serviceId);
        return specialists.stream().map(SpecialistMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SpecialistDto> getSpecialistsByCity(String city) {
        log.info("Fetching specialists in city: {}", city);
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        List<Specialist> specialists = specialistRepository.findByWorkAddress_City(city);
        return specialists.stream().map(SpecialistMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SpecialistDto updateSpecialist(Long id, CreateSpecialistRequest request) {
        log.info("Updating specialist with id: {}", id);
        validateSpecialistRequest(request);
        Specialist existingSpecialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        if (!existingSpecialist.getUser().getId().equals(request.getUserId()) &&
                isUserAlreadySpecialist(request.getUserId())) {
            throw new SpecialistAlreadyExistsException("User with id " + request.getUserId() + " is already a specialist");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
        Address workAddress = null;
        if (request.getWorkAddressId() != null) {
            workAddress = addressRepository.findById(request.getWorkAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + request.getWorkAddressId()));
        }
        existingSpecialist.setUser(user);
        existingSpecialist.setSpecialization(request.getSpecialization());
        existingSpecialist.setWorkAddress(workAddress);
        Specialist updatedSpecialist = specialistRepository.save(existingSpecialist);
        log.info("Specialist updated successfully with id: {}", updatedSpecialist.getId());
        return SpecialistMapper.toDto(updatedSpecialist);
    }

    @Override
    @Transactional
    public SpecialistDto updateSpecialization(Long id, String specialization) {
        log.info("Updating specialization for specialist: {} to: {}", id, specialization);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        specialist.setSpecialization(specialization.trim());
        Specialist updatedSpecialist = specialistRepository.save(specialist);
        log.info("Specialization updated successfully for id: {}", id);
        return SpecialistMapper.toDto(updatedSpecialist);
    }

    @Override
    @Transactional
    public SpecialistDto updateWorkAddress(Long id, Long addressId) {
        log.info("Updating work address for specialist: {} to address: {}", id, addressId);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        Address workAddress = null;
        if (addressId != null) {
            workAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        }
        specialist.setWorkAddress(workAddress);
        Specialist updatedSpecialist = specialistRepository.save(specialist);
        log.info("Work address updated successfully for specialist: {}", id);
        return SpecialistMapper.toDto(updatedSpecialist);
    }

    @Override
    @Transactional
    public SpecialistDto enableAppointments(Long id) {
        log.info("Enabling appointments for specialist: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        if (specialist.isAcceptingAppointments()) {
            throw new IllegalStateException("Specialist is already accepting appointments");
        }
        specialist.setAcceptingAppointments(true);
        Specialist updatedSpecialist = specialistRepository.save(specialist);
        log.info("Appointments enabled for specialist: {}", id);
        return SpecialistMapper.toDto(updatedSpecialist);
    }

    @Override
    @Transactional
    public SpecialistDto disableAppointments(Long id) {
        log.info("Disabling appointments for specialist: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        if (!specialist.isAcceptingAppointments()) {
            throw new IllegalStateException("Specialist is already not accepting appointments");
        }
        specialist.setAcceptingAppointments(false);
        Specialist updatedSpecialist = specialistRepository.save(specialist);
        log.info("Appointments disabled for specialist: {}", id);
        return SpecialistMapper.toDto(updatedSpecialist);
    }

    @Override
    @Transactional
    public void deleteSpecialist(Long id) {
        log.info("Deleting specialist with id: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found with id: " + id));
        boolean hasActiveAppointments = appointmentRepository.existsBySpecialistIdAndStatusIn(
                id, List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED));
        if (hasActiveAppointments) {
            throw new IllegalStateException("Cannot delete specialist with active appointments. Disable appointments instead.");
        }
        specialistRepository.deleteById(id);
        log.info("Specialist deleted successfully: {}", id);
    }

    @Override
    public boolean isUserAlreadySpecialist(Long userId) {
        log.debug("Checking if user: {} is already a specialist", userId);
        return specialistRepository.existsByUserId(userId);
    }

    @Override
    public SpecialistDto getSpecialistByUserId(Long userId) {
        log.info("Fetching specialist by user id: {}", userId);
        Specialist specialist = specialistRepository.findByUserId(userId)
                .orElseThrow(() -> new SpecialistNotFoundException("No specialist found for user with id: " + userId));
        return SpecialistMapper.toDto(specialist);
    }

    private void validateSpecialistRequest(CreateSpecialistRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (request.getSpecialization() == null || request.getSpecialization().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }

        if (request.getSpecialization().length() < 2) {
            throw new IllegalArgumentException("Specialization must be at least 2 characters long");
        }
    }
}
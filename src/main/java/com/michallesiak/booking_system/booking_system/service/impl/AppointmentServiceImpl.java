package com.michallesiak.booking_system.booking_system.service.impl;

import com.michallesiak.booking_system.booking_system.dto.AppointmentDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAppointmentRequest;
import com.michallesiak.booking_system.booking_system.exception.AppointmentNotFoundException;
import com.michallesiak.booking_system.booking_system.exception.SlotNotAvailableException;
import com.michallesiak.booking_system.booking_system.mapper.AppointmentMapper;
import com.michallesiak.booking_system.booking_system.model.Appointment;
import com.michallesiak.booking_system.booking_system.model.AvailableSlot;
import com.michallesiak.booking_system.booking_system.model.OfferedService;
import com.michallesiak.booking_system.booking_system.model.User;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import com.michallesiak.booking_system.booking_system.repository.AppointmentRepository;
import com.michallesiak.booking_system.booking_system.repository.AvailableSlotRepository;
import com.michallesiak.booking_system.booking_system.repository.OfferedServiceRepository;
import com.michallesiak.booking_system.booking_system.repository.UserRepository;
import com.michallesiak.booking_system.booking_system.service.AppointmentService;
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
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final OfferedServiceRepository offeredServiceRepository;

    @Override
    @Transactional
    public AppointmentDto createAppointment(CreateAppointmentRequest request) {
        log.info("Creating appointment for user: {}, slot: {}, service: {}",
                request.getUserId(), request.getSlotId(), request.getServiceId());
        if (!isSlotAvailable(request.getSlotId())) {
            throw new SlotNotAvailableException("Slot with id " + request.getSlotId() + " is not available");
        }
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
        AvailableSlot slot = availableSlotRepository.findById(request.getSlotId()).orElseThrow(() -> new EntityNotFoundException("Slot not found with id: " + request.getSlotId()));
        OfferedService service = offeredServiceRepository.findById(request.getServiceId()).orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + request.getServiceId()));
        Appointment appointment = AppointmentMapper.toEntity(request, user, slot, service);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with id: {}", savedAppointment.getId());
        return AppointmentMapper.toDto(savedAppointment);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        log.info("Fetching appointment with id: {}", id);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        log.info("Fetching all appointments");

        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByUserId(Long userId) {
        log.info("Fetching appointments for user: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return appointments.stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByStatus(AppointmentStatus status) {
        log.info("Fetching appointments with status: {}", status);
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return appointments.stream().map(AppointmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointmentStatus(Long id, AppointmentStatus status) {
        log.info("Updating appointment status for id: {} to: {}", id, status);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment status updated successfully for id: {}", id);
        return AppointmentMapper.toDto(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointmentNotes(Long id, String notes) {
        log.info("Updating appointment notes for id: {}", id);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        appointment.setNotes(notes);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment notes updated successfully for id: {}", id);
        return AppointmentMapper.toDto(updatedAppointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id) {
        log.info("Cancelling appointment with id: {}", id);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        log.info("Appointment cancelled successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with id: {}", id);
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
        log.info("Appointment deleted successfully with id: {}", id);
    }

    @Override
    public boolean isSlotAvailable(Long slotId) {
        log.info("Checking availability for slot: {}", slotId);
        if (!availableSlotRepository.existsById(slotId)) {
            return false;
        }
        boolean isBooked = appointmentRepository.existsBySlotIdAndStatusIn(
                slotId,
                List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)
        );
        return !isBooked;
    }
}
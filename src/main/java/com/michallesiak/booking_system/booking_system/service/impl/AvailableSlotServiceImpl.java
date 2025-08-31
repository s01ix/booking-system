package com.michallesiak.booking_system.booking_system.service.impl;

import com.michallesiak.booking_system.booking_system.dto.AvailableSlotDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAvailableSlotRequest;
import com.michallesiak.booking_system.booking_system.exception.SlotConflictException;
import com.michallesiak.booking_system.booking_system.exception.SlotNotFoundException;
import com.michallesiak.booking_system.booking_system.mapper.AvailableSlotMapper;
import com.michallesiak.booking_system.booking_system.model.AvailableSlot;
import com.michallesiak.booking_system.booking_system.model.Specialist;
import com.michallesiak.booking_system.booking_system.repository.AvailableSlotRepository;
import com.michallesiak.booking_system.booking_system.repository.SpecialistRepository;
import com.michallesiak.booking_system.booking_system.service.AvailableSlotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AvailableSlotServiceImpl implements AvailableSlotService {
    private final AvailableSlotRepository availableSlotRepository;
    private final SpecialistRepository specialistRepository;
    @Override
    @Transactional
    public AvailableSlotDto createSlot(CreateAvailableSlotRequest request) {
        log.info("Creating slot for specialist: {} from {} to {}",
                request.getSpecialistId(), request.getStartTime(), request.getEndTime());
        validateSlotTimes(request.getStartTime(), request.getEndTime());
        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found with id: " + request.getSpecialistId()));
        if (isSlotConflicting(request.getSpecialistId(), request.getStartTime(), request.getEndTime(), null)) {
            throw new SlotConflictException("Time slot conflicts with existing slots");
        }
        AvailableSlot slot = AvailableSlotMapper.toEntity(request, specialist);
        AvailableSlot savedSlot = availableSlotRepository.save(slot);
        log.info("Slot created successfully with id: {}", savedSlot.getId());
        return AvailableSlotMapper.toDto(savedSlot);
    }

    @Override
    public AvailableSlotDto getSlotById(Long id) {
        log.info("Fetching slot with id: {}", id);
        AvailableSlot slot = availableSlotRepository.findById(id).orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + id));
        return AvailableSlotMapper.toDto(slot);
    }

    @Override
    public List<AvailableSlotDto> getAllSlots() {
        log.info("Fetching all slots");
        List<AvailableSlot> slots = availableSlotRepository.findAll();
        return slots.stream().map(AvailableSlotMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AvailableSlotDto> getSlotsBySpecialistId(Long specialistId) {
        log.info("Fetching all slots for specialist: {}", specialistId);
        if (!specialistRepository.existsById(specialistId)) {
            throw new EntityNotFoundException("Specialist not found with id: " + specialistId);
        }
        List<AvailableSlot> slots = availableSlotRepository.findBySpecialistId(specialistId);
        return slots.stream().map(AvailableSlotMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AvailableSlotDto> getAvailableSlotsBySpecialistId(Long specialistId) {
        log.info("Fetching available slots for specialist: {}", specialistId);
        if (!specialistRepository.existsById(specialistId)) {
            throw new EntityNotFoundException("Specialist not found with id: " + specialistId);
        }

        List<AvailableSlot> slots = availableSlotRepository.findBySpecialistIdAndBookedFalse(specialistId);
        return slots.stream().map(AvailableSlotMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AvailableSlotDto> getSlotsBySpecialistAndDateRange(Long specialistId, LocalDateTime start, LocalDateTime end) {
        log.info("Fetching slots for specialist: {} between {} and {}", specialistId, start, end);
        if (!specialistRepository.existsById(specialistId)) {
            throw new EntityNotFoundException("Specialist not found with id: " + specialistId);
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        List<AvailableSlot> slots = availableSlotRepository.findBySpecialistIdAndStartTimeBetween(specialistId, start, end);
        return slots.stream().map(AvailableSlotMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AvailableSlotDto> getAvailableSlotsBySpecialistAndDate(Long specialistId, LocalDate date) {
        log.info("Fetching available slots for specialist: {} on date: {}", specialistId, date);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<AvailableSlot> slots = availableSlotRepository.findBySpecialistIdAndStartTimeBetweenAndBookedFalse(
                specialistId, startOfDay, endOfDay);
        return slots.stream().map(AvailableSlotMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailableSlotDto markSlotAsBooked(Long id) {
        log.info("Marking slot as booked: {}", id);
        AvailableSlot slot = availableSlotRepository.findById(id).orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + id));
        if (slot.isBooked()) {
            throw new IllegalStateException("Slot is already booked");
        }
        slot.setBooked(true);
        AvailableSlot updatedSlot = availableSlotRepository.save(slot);
        log.info("Slot marked as booked: {}", id);
        return AvailableSlotMapper.toDto(updatedSlot);
    }

    @Override
    @Transactional
    public AvailableSlotDto markSlotAsAvailable(Long id) {
        log.info("Marking slot as available: {}", id);
        AvailableSlot slot = availableSlotRepository.findById(id).orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + id));
        slot.setBooked(false);
        AvailableSlot updatedSlot = availableSlotRepository.save(slot);
        log.info("Slot marked as available: {}", id);
        return AvailableSlotMapper.toDto(updatedSlot);
    }

    @Override
    @Transactional
    public List<AvailableSlotDto> createMultipleSlots(
            Long specialistId, LocalDateTime startTime, LocalDateTime endTime, int durationMinutes) {
        log.info("Creating multiple slots for specialist: {} from {} to {} with duration: {} minutes",
                specialistId, startTime, endTime, durationMinutes);
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found with id: " + specialistId));
        List<AvailableSlot> slots = new ArrayList<>();
        LocalDateTime current = startTime;
        while (current.plusMinutes(durationMinutes).isBefore(endTime) || current.plusMinutes(durationMinutes).equals(endTime)) {
            LocalDateTime slotEnd = current.plusMinutes(durationMinutes);
            if (!isSlotConflicting(specialistId, current, slotEnd, null)) {
                AvailableSlot slot = new AvailableSlot();
                slot.setSpecialist(specialist);
                slot.setStartTime(current);
                slot.setEndTime(slotEnd);
                slot.setBooked(false);
                slots.add(slot);
            } else {
                log.warn("Skipping conflicting slot from {} to {}", current, slotEnd);
            }
            current = current.plusMinutes(durationMinutes);
        }
        if (slots.isEmpty()) {
            throw new SlotConflictException("No slots could be created due to conflicts");
        }
        List<AvailableSlot> savedSlots = availableSlotRepository.saveAll(slots);
        log.info("Created {} slots successfully", savedSlots.size());
        return savedSlots.stream()
                .map(AvailableSlotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSlot(Long id) {
        log.info("Deleting slot with id: {}", id);
        AvailableSlot slot = availableSlotRepository.findById(id).orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + id));
        if (slot.isBooked()) {
            throw new IllegalStateException("Cannot delete booked slot");
        }
        availableSlotRepository.deleteById(id);
        log.info("Slot deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteSlotsBySpecialistAndDateRange(Long specialistId, LocalDateTime start, LocalDateTime end) {
        log.info("Deleting slots for specialist: {} between {} and {}", specialistId, start, end);
        List<AvailableSlot> slots = availableSlotRepository.findBySpecialistIdAndStartTimeBetween(specialistId, start, end);
        List<AvailableSlot> bookedSlots = slots.stream()
                .filter(AvailableSlot::isBooked)
                .collect(Collectors.toList());
        if (!bookedSlots.isEmpty()) {
            throw new IllegalStateException("Cannot delete slots - some slots are booked");
        }
        availableSlotRepository.deleteAll(slots);
        log.info("Deleted {} slots for specialist: {}", slots.size(), specialistId);
    }

    @Override
    public boolean isSlotConflicting(Long specialistId, LocalDateTime startTime, LocalDateTime endTime, Long excludeSlotId) {
        log.debug("Checking slot conflicts for specialist: {} from {} to {}", specialistId, startTime, endTime);
        List<AvailableSlot> existingSlots = availableSlotRepository.findBySpecialistIdAndStartTimeBetween(
                specialistId, startTime.minusMinutes(1), endTime.plusMinutes(1));
        return existingSlots.stream()
                .filter(slot -> excludeSlotId == null || !slot.getId().equals(excludeSlotId))
                .anyMatch(slot -> isTimeOverlapping(startTime, endTime, slot.getStartTime(), slot.getEndTime()));
    }

    private boolean isTimeOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private void validateSlotTimes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time cannot equal end time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create slots in the past");
        }
    }
    @Override
    public boolean isSlotAvailable(Long slotId) {
        log.info("Checking availability for slot: {}", slotId);

        AvailableSlot slot = availableSlotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + slotId));

        return !slot.isBooked();
    }
}
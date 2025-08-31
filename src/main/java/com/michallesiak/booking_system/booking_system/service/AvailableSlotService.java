package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.AvailableSlotDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAvailableSlotRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AvailableSlotService {
    AvailableSlotDto createSlot(CreateAvailableSlotRequest request);
    AvailableSlotDto getSlotById(Long id);
    List<AvailableSlotDto> getAllSlots();
    List<AvailableSlotDto> getSlotsBySpecialistId(Long specialistId);
    List<AvailableSlotDto> getAvailableSlotsBySpecialistId(Long specialistId);
    List<AvailableSlotDto> getSlotsBySpecialistAndDateRange(Long specialistId, LocalDateTime start, LocalDateTime end);
    List<AvailableSlotDto> getAvailableSlotsBySpecialistAndDate(Long specialistId, LocalDate date);
    AvailableSlotDto markSlotAsBooked(Long id);
    AvailableSlotDto markSlotAsAvailable(Long id);
    List<AvailableSlotDto> createMultipleSlots(Long specialistId, LocalDateTime startTime, LocalDateTime endTime, int durationMinutes);
    void deleteSlot(Long id);
    void deleteSlotsBySpecialistAndDateRange(Long specialistId, LocalDateTime start, LocalDateTime end);
    boolean isSlotConflicting(Long specialistId, LocalDateTime startTime, LocalDateTime endTime, Long excludeSlotId);
    boolean isSlotAvailable(Long slotId);
}
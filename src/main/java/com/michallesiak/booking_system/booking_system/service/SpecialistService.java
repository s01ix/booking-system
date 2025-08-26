package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.CreateSpecialistRequest;
import com.michallesiak.booking_system.booking_system.dto.SpecialistDto;

import java.util.List;

public interface SpecialistService {

    SpecialistDto createSpecialist(CreateSpecialistRequest request);

    SpecialistDto getSpecialistById(Long id);

    List<SpecialistDto> getAllSpecialists();

    List<SpecialistDto> getActiveSpecialists();

    List<SpecialistDto> getSpecialistsBySpecialization(String specialization);

    List<SpecialistDto> getSpecialistsByServiceId(Long serviceId);

    List<SpecialistDto> getSpecialistsByCity(String city);

    SpecialistDto updateSpecialist(Long id, CreateSpecialistRequest request);

    SpecialistDto updateSpecialization(Long id, String specialization);

    SpecialistDto updateWorkAddress(Long id, Long addressId);

    SpecialistDto enableAppointments(Long id);

    SpecialistDto disableAppointments(Long id);

    void deleteSpecialist(Long id);

    boolean isUserAlreadySpecialist(Long userId);

    SpecialistDto getSpecialistByUserId(Long userId);
}
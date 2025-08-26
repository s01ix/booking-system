package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.CreateOfferedServiceRequest;
import com.michallesiak.booking_system.booking_system.dto.OfferedServiceDto;

import java.math.BigDecimal;
import java.util.List;

public interface OfferedServiceService {

    OfferedServiceDto createService(CreateOfferedServiceRequest request);

    OfferedServiceDto getServiceById(Long id);

    List<OfferedServiceDto> getAllServices();

    List<OfferedServiceDto> getActiveServices();

    List<OfferedServiceDto> getServicesBySpecialistId(Long specialistId);

    OfferedServiceDto updateService(Long id, CreateOfferedServiceRequest request);

    OfferedServiceDto updateServicePrice(Long id, BigDecimal newPrice);

    OfferedServiceDto activateService(Long id);

    OfferedServiceDto deactivateService(Long id);

    void deleteService(Long id);

    boolean existsByName(String name);

    List<OfferedServiceDto> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<OfferedServiceDto> getServicesByDuration(Integer durationMinutes);
}
package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.dto.CreateOfferedServiceRequest;
import com.michallesiak.booking_system.booking_system.dto.OfferedServiceDto;
import com.michallesiak.booking_system.booking_system.model.OfferedService;

public class OfferedServiceMapper {
    public static OfferedServiceDto toDto(OfferedService entity) {
        return new OfferedServiceDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDurationMinutes(),
                entity.isActive()
        );
    }

    public static OfferedService toEntity(CreateOfferedServiceRequest request) {
        OfferedService service = new OfferedService();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setActive(true);
        return service;
    }
}

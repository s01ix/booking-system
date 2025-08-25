package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.dto.AvailableSlotDto;
import com.michallesiak.booking_system.booking_system.model.AvailableSlot;
import com.michallesiak.booking_system.booking_system.dto.CreateAvailableSlotRequest;
import com.michallesiak.booking_system.booking_system.model.Specialist;

public class AvailableSlotMapper {
    public static AvailableSlotDto toDto(AvailableSlot entity) {
        return new AvailableSlotDto(
                entity.getId(),
                entity.getSpecialist().getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.isBooked()
        );
    }

    public static AvailableSlot toEntity(CreateAvailableSlotRequest request, Specialist specialist) {
        AvailableSlot slot = new AvailableSlot();
        slot.setSpecialist(specialist);
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setBooked(false);
        return slot;
    }
}

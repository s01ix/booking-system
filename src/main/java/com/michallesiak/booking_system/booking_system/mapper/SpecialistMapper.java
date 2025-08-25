package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.dto.CreateSpecialistRequest;
import com.michallesiak.booking_system.booking_system.dto.SpecialistDto;
import com.michallesiak.booking_system.booking_system.model.Address;
import com.michallesiak.booking_system.booking_system.model.Specialist;
import com.michallesiak.booking_system.booking_system.model.User;

public class SpecialistMapper {
    public static SpecialistDto toDto(Specialist entity) {
        return new SpecialistDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getSpecialization(),
                entity.getWorkAddress() != null ? entity.getWorkAddress().getId() : null,
                entity.isAcceptingAppointments()
        );
    }

    public static Specialist toEntity(CreateSpecialistRequest request, User user, Address address) {
        Specialist specialist = new Specialist();
        specialist.setUser(user);
        specialist.setSpecialization(request.getSpecialization());
        specialist.setWorkAddress(address);
        specialist.setAcceptingAppointments(true);
        return specialist;
    }
}

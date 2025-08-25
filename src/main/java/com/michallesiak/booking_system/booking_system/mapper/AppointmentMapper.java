package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.dto.AppointmentDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAppointmentRequest;
import com.michallesiak.booking_system.booking_system.model.Appointment;
import com.michallesiak.booking_system.booking_system.model.AvailableSlot;
import com.michallesiak.booking_system.booking_system.model.OfferedService;
import com.michallesiak.booking_system.booking_system.model.User;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;

public class AppointmentMapper {
    public static AppointmentDto toDto(Appointment entity) {
        return new AppointmentDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getSlot().getId(),
                entity.getService().getId(),
                entity.getStatus(),
                entity.getNotes()
        );
    }

    public static Appointment toEntity(CreateAppointmentRequest request, User user, AvailableSlot slot, OfferedService service) {
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setSlot(slot);
        appointment.setService(service);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setNotes(request.getNotes());
        return appointment;
    }
}

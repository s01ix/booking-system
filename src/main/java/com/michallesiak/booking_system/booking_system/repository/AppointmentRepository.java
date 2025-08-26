package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.Appointment;
import com.michallesiak.booking_system.booking_system.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByStatus(AppointmentStatus status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.slot.id = :slotId AND a.status IN :statuses")
    boolean existsBySlotIdAndStatusIn(@Param("slotId") Long slotId, @Param("statuses") List<AppointmentStatus> statuses);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.service.id = :serviceId AND a.status IN :statuses")
    boolean existsByServiceIdAndStatusIn(@Param("serviceId") Long serviceId, @Param("statuses") List<AppointmentStatus> statuses);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.slot.specialist.id = :specialistId AND a.status IN :statuses")
    boolean existsBySpecialistIdAndStatusIn(@Param("specialistId") Long specialistId, @Param("statuses") List<AppointmentStatus> statuses);

    List<Appointment> findByServiceId(Long serviceId);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.status = :status")
    List<Appointment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") AppointmentStatus status);
}
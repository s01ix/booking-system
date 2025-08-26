package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.AvailableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {

    List<AvailableSlot> findBySpecialistIdAndStartTimeBetween(Long specialistId, LocalDateTime start, LocalDateTime end);

    List<AvailableSlot> findBySpecialistId(Long specialistId);

    List<AvailableSlot> findBySpecialistIdAndBookedFalse(Long specialistId);

    List<AvailableSlot> findBySpecialistIdAndStartTimeBetweenAndBookedFalse(
            Long specialistId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM AvailableSlot s WHERE s.specialist.id = :specialistId " +
            "AND s.booked = false AND s.startTime >= :from ORDER BY s.startTime")
    List<AvailableSlot> findAvailableSlotsFromDate(@Param("specialistId") Long specialistId,
                                                   @Param("from") LocalDateTime from);

    @Query("SELECT COUNT(s) > 0 FROM AvailableSlot s WHERE s.specialist.id = :specialistId " +
            "AND ((s.startTime < :endTime AND s.endTime > :startTime)) " +
            "AND (:excludeId IS NULL OR s.id != :excludeId)")
    boolean existsConflictingSlot(@Param("specialistId") Long specialistId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  @Param("excludeId") Long excludeId);
}
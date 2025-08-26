package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    boolean existsByUserId(Long userId);

    Optional<Specialist> findByUserId(Long userId);

    List<Specialist> findByAcceptingAppointmentsTrue();

    List<Specialist> findBySpecializationContainingIgnoreCase(String specialization);

    @Query("SELECT s FROM Specialist s JOIN s.services serv WHERE serv.id = :serviceId AND s.acceptingAppointments = true")
    List<Specialist> findByServicesIdAndAcceptingAppointmentsTrue(@Param("serviceId") Long serviceId);

    List<Specialist> findByWorkAddress_City(String city);

    @Query("SELECT s FROM Specialist s WHERE s.workAddress.city = :city AND s.acceptingAppointments = true")
    List<Specialist> findByCityAndAcceptingAppointments(@Param("city") String city);

    @Query("SELECT s FROM Specialist s WHERE s.specialization ILIKE %:specialization% AND s.acceptingAppointments = true")
    List<Specialist> findBySpecializationAndAcceptingAppointments(@Param("specialization") String specialization);

    @Query("SELECT DISTINCT s.specialization FROM Specialist s WHERE s.acceptingAppointments = true ORDER BY s.specialization")
    List<String> findDistinctSpecializations();

    @Query("SELECT s FROM Specialist s JOIN s.services serv WHERE serv.active = true AND s.acceptingAppointments = true")
    List<Specialist> findSpecialistsWithActiveServices();
}
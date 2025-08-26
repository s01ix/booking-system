package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {

    boolean existsByName(String name);

    List<OfferedService> findByActiveTrue();

    List<OfferedService> findByActiveFalse();

    @Query("SELECT s FROM OfferedService s JOIN s.specialists sp WHERE sp.id = :specialistId AND s.active = true")
    List<OfferedService> findBySpecialistsIdAndActiveTrue(@Param("specialistId") Long specialistId);

    List<OfferedService> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    List<OfferedService> findByDurationMinutesAndActiveTrue(Integer durationMinutes);

    List<OfferedService> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    @Query("SELECT s FROM OfferedService s WHERE s.price <= :maxPrice AND s.active = true ORDER BY s.price")
    List<OfferedService> findByPriceLessThanEqualAndActiveTrueOrderByPrice(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT s FROM OfferedService s WHERE s.durationMinutes <= :maxDuration AND s.active = true ORDER BY s.durationMinutes")
    List<OfferedService> findByDurationLessThanEqualAndActiveTrueOrderByDuration(@Param("maxDuration") Integer maxDuration);
}
package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {
    boolean existsByName(String name);
}

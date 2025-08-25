package com.michallesiak.booking_system.booking_system.repository;

import com.michallesiak.booking_system.booking_system.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface AddressRepository extends JpaRepository<Address, Long> {
}
package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.model.Address;
import com.michallesiak.booking_system.booking_system.dto.AddressDto;

public class AddressMapper {

    public static AddressDto toDto(Address entity) {
        return new AddressDto(
                entity.getId(),
                entity.getCountry(),
                entity.getCity(),
                entity.getStreet(),
                entity.getPostalCode(),
                entity.getBuildingNumber()
        );
    }

    public static Address toEntity(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setCountry(dto.getCountry());
        address.setCity(dto.getCity());
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        address.setBuildingNumber(dto.getBuildingNumber());
        return address;
    }
}


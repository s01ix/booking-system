package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.AddressDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAddressRequest;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(CreateAddressRequest request);
    AddressDto getAddressById(Long id);
    List<AddressDto> getAllAddresses();
    AddressDto updateAddress(Long id, CreateAddressRequest request);
    void deleteAddress(Long id);
}
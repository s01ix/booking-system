package com.michallesiak.booking_system.booking_system.service.impl;

import com.michallesiak.booking_system.booking_system.dto.AddressDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAddressRequest;
import com.michallesiak.booking_system.booking_system.mapper.AddressMapper;
import com.michallesiak.booking_system.booking_system.model.Address;
import com.michallesiak.booking_system.booking_system.repository.AddressRepository;
import com.michallesiak.booking_system.booking_system.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public AddressDto createAddress(CreateAddressRequest request) {
        log.info("Creating address for country: {} and city: {}", request.getCountry(), request.getCity());
        Address address = AddressMapper.toEntity(request);
        Address savedAddress = addressRepository.save(address);
        log.info("Address created with id: {}", savedAddress.getId());
        return AddressMapper.toDto(savedAddress);
    }

    @Override
    public AddressDto getAddressById(Long id) {
        log.info("Fetching address with id: {}", id);
        Address address = addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        return AddressMapper.toDto(address);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        log.info("Fetching all addresses");
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(AddressMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long id, CreateAddressRequest request) {
        log.info("Updating address with id: {}", id);
        Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        existingAddress.setCountry(request.getCountry());
        existingAddress.setCity(request.getCity());
        existingAddress.setStreet(request.getStreet());
        existingAddress.setPostalCode(request.getPostalCode());
        existingAddress.setBuildingNumber(request.getBuildingNumber());
        Address updatedAddress = addressRepository.save(existingAddress);
        log.info("Address updated with id: {}", updatedAddress.getId());
        return AddressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deleting address with id: {}", id);
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
        log.info("Address deleted with id: {}", id);
    }
}
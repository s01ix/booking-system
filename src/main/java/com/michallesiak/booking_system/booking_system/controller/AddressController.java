package com.michallesiak.booking_system.booking_system.controller;

import com.michallesiak.booking_system.booking_system.dto.AddressDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAddressRequest;
import com.michallesiak.booking_system.booking_system.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        log.info("REST request to create address for country: {}, city: {}", request.getCountry(), request.getCity());

        AddressDto createdAddress = addressService.createAddress(request);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        log.info("REST request to get address with id: {}", id);

        AddressDto address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        log.info("REST request to get all addresses");

        List<AddressDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id,
                                                    @Valid @RequestBody CreateAddressRequest request) {
        log.info("REST request to update address with id: {}", id);

        AddressDto updatedAddress = addressService.updateAddress(id, request);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.info("REST request to delete address with id: {}", id);

        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
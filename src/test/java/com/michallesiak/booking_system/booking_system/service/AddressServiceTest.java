package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.AddressDto;
import com.michallesiak.booking_system.booking_system.dto.CreateAddressRequest;
import com.michallesiak.booking_system.booking_system.model.Address;
import com.michallesiak.booking_system.booking_system.repository.AddressRepository;
import com.michallesiak.booking_system.booking_system.service.impl.AddressServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service Tests")
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private CreateAddressRequest createRequest;
    private Address address;
    private AddressDto expectedDto;

    @BeforeEach
    void setUp() {
        createRequest = new CreateAddressRequest(
                "Poland",
                "Warsaw",
                "Marszałkowska 1",
                "00-001",
                "1A"
        );

        address = new Address();
        address.setId(1L);
        address.setCountry("Poland");
        address.setCity("Warsaw");
        address.setStreet("Marszałkowska 1");
        address.setPostalCode("00-001");
        address.setBuildingNumber("1A");
        expectedDto = new AddressDto(1L, "Poland", "Warsaw", "Marszałkowska 1", "00-001", "1A");
    }

    @Test
    @DisplayName("Should create address successfully")
    void shouldCreateAddressSuccessfully() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        AddressDto result = addressService.createAddress(createRequest);
        assertThat(result).isNotNull();
        assertThat(result.getCountry()).isEqualTo("Poland");
        assertThat(result.getCity()).isEqualTo("Warsaw");
        assertThat(result.getId()).isEqualTo(1L);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Should get address by ID successfully")
    void shouldGetAddressByIdSuccessfully() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        AddressDto result = addressService.getAddressById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCountry()).isEqualTo("Poland");
        verify(addressRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when address not found")
    void shouldThrowExceptionWhenAddressNotFound() {
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> addressService.getAddressById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Address not found with id: 999");
        verify(addressRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get all addresses successfully")
    void shouldGetAllAddressesSuccessfully() {
        Address address2 = new Address();
        address2.setId(2L);
        address2.setCountry("Germany");
        address2.setCity("Berlin");
        List<Address> addresses = Arrays.asList(address, address2);
        when(addressRepository.findAll()).thenReturn(addresses);
        List<AddressDto> result = addressService.getAllAddresses();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCountry()).isEqualTo("Poland");
        assertThat(result.get(1).getCountry()).isEqualTo("Germany");
        verify(addressRepository).findAll();
    }

    @Test
    @DisplayName("Should update address successfully")
    void shouldUpdateAddressSuccessfully() {
        CreateAddressRequest updateRequest = new CreateAddressRequest(
                "Germany", "Berlin", "Alexanderplatz 1", "10178", "5"
        );

        Address updatedAddress = new Address();
        updatedAddress.setId(1L);
        updatedAddress.setCountry("Germany");
        updatedAddress.setCity("Berlin");
        updatedAddress.setStreet("Alexanderplatz 1");
        updatedAddress.setPostalCode("10178");
        updatedAddress.setBuildingNumber("5");
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        AddressDto result = addressService.updateAddress(1L, updateRequest);
        assertThat(result).isNotNull();
        assertThat(result.getCountry()).isEqualTo("Germany");
        assertThat(result.getCity()).isEqualTo("Berlin");
        verify(addressRepository).findById(1L);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing address")
    void shouldThrowExceptionWhenUpdatingNonExistingAddress() {
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> addressService.updateAddress(999L, createRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Address not found with id: 999");
        verify(addressRepository).findById(999L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    @DisplayName("Should delete address successfully")
    void shouldDeleteAddressSuccessfully() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        addressService.deleteAddress(1L);
        verify(addressRepository).existsById(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existing address")
    void shouldThrowExceptionWhenDeletingNonExistingAddress() {
        when(addressRepository.existsById(999L)).thenReturn(false);
        assertThatThrownBy(() -> addressService.deleteAddress(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Address not found with id: 999");
        verify(addressRepository).existsById(999L);
        verify(addressRepository, never()).deleteById(anyLong());
    }
}
package com.michallesiak.booking_system.booking_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    @Size(max = 200, message = "Street cannot exceed 200 characters")
    private String street;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$|^$", message = "Postal code must be in format XX-XXX or empty")
    private String postalCode;

    @Size(max = 20, message = "Building number cannot exceed 20 characters")
    private String buildingNumber;
}
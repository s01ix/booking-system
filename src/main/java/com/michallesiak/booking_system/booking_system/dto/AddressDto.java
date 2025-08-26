package com.michallesiak.booking_system.booking_system.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String country;
    private String city;
    private String street;
    private String postalCode;
    private String buildingNumber;
}

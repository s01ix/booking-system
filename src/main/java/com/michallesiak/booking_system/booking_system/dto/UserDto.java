package com.michallesiak.booking_system.booking_system.dto;

import com.michallesiak.booking_system.booking_system.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private Set<Role> roles;
    private boolean enabled;
}

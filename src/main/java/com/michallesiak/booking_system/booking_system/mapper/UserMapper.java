package com.michallesiak.booking_system.booking_system.mapper;

import com.michallesiak.booking_system.booking_system.dto.CreateUserRequest;
import com.michallesiak.booking_system.booking_system.dto.UserDto;
import com.michallesiak.booking_system.booking_system.model.User;

public class UserMapper {
    public static UserDto toDto(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getPhone(),
                entity.getRoles(),
                entity.isEnabled()
        );
    }

    public static User toEntity(CreateUserRequest request, String encodedPassword) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword); // ważne: zawsze trzymamy hasła zahashowane
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhone(request.getPhone());
        user.setEnabled(true);
        return user;
    }
}

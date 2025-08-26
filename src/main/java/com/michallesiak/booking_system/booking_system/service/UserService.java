package com.michallesiak.booking_system.booking_system.service;

import com.michallesiak.booking_system.booking_system.dto.CreateUserRequest;
import com.michallesiak.booking_system.booking_system.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequest request);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, CreateUserRequest request);
    void deleteUser(Long id);
    void changePassword(Long userId, String currentPassword, String newPassword);
}

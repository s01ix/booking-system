package com.michallesiak.booking_system.booking_system.exception;

public class SpecialistAlreadyExistsException extends RuntimeException {
    public SpecialistAlreadyExistsException(String message) {
        super(message);
    }

    public SpecialistAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
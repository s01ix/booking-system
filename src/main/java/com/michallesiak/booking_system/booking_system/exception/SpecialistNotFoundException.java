package com.michallesiak.booking_system.booking_system.exception;

public class SpecialistNotFoundException extends RuntimeException {
    public SpecialistNotFoundException(String message) {
        super(message);
    }

    public SpecialistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

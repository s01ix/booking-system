package com.michallesiak.booking_system.booking_system.exception;

public class ServiceAlreadyExistsException extends RuntimeException {
    public ServiceAlreadyExistsException(String message) {
        super(message);
    }

    public ServiceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
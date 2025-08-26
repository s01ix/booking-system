package com.michallesiak.booking_system.booking_system.exception;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException(String message) {
        super(message);
    }

    public SlotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
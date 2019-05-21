package com.challenge.carparkubi.chargingpoint.exception;

/**
 * Custom exception to use if a charging point is missing
 */
public class ChargingPointNotFoundException extends Exception {
    public ChargingPointNotFoundException() {
        super();
    }

    public ChargingPointNotFoundException(String message) {
        super(message);
    }

    public ChargingPointNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChargingPointNotFoundException(Throwable cause) {
        super(cause);
    }
}

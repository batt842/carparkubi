package com.challenge.carparkubi.chargingpoint.exception;

/**
 * Custom exception to use if a charging point is unavailable
 */
public class ChargingPointUnavailableException extends Exception {
    public ChargingPointUnavailableException() {
        super();
    }

    public ChargingPointUnavailableException(String message) {
        super(message);
    }

    public ChargingPointUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChargingPointUnavailableException(Throwable cause) {
        super(cause);
    }
}

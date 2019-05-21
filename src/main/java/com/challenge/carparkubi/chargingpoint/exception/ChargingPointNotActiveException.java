package com.challenge.carparkubi.chargingpoint.exception;

/**
 * Custom exception to use if a charging point is not used
 */
public class ChargingPointNotActiveException extends Exception {
    public ChargingPointNotActiveException() {
        super();
    }

    public ChargingPointNotActiveException(String message) {
        super(message);
    }

    public ChargingPointNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChargingPointNotActiveException(Throwable cause) {
        super(cause);
    }
}

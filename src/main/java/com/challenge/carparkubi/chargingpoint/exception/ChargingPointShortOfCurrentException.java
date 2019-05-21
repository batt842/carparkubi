package com.challenge.carparkubi.chargingpoint.exception;

/**
 * Custom exception to use if a charging point doesn't have enough current
 */
public class ChargingPointShortOfCurrentException extends Exception {
    public ChargingPointShortOfCurrentException() {
        super();
    }

    public ChargingPointShortOfCurrentException(String message) {
        super(message);
    }

    public ChargingPointShortOfCurrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChargingPointShortOfCurrentException(Throwable cause) {
        super(cause);
    }
}

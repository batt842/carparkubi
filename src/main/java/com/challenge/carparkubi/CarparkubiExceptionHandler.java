package com.challenge.carparkubi;

import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotActiveException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotFoundException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CarparkubiExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(CarparkubiExceptionHandler.class);


    @ExceptionHandler(value = {ChargingPointUnavailableException.class})
    protected ResponseEntity<Object> handleChargingPointUnavailable(Exception e) {
        return handleExceptionInternal(e, "Resource already occupied", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ChargingPointNotActiveException.class})
    protected ResponseEntity<Object> handleChargingPointNotActive(Exception e) {
        return handleExceptionInternal(e, "Resource not occupied", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ChargingPointNotFoundException.class})
    protected ResponseEntity<Object> handleChargingPointNotFound(Exception e) {
        return handleExceptionInternal(e, "Resource not found", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, String message, HttpStatus httpStatus) {
        logger.error(message, e);
        Map<String, String> result = new HashMap<String, String>() {
            {
                put("message", message);
            }
        };
        return new ResponseEntity<>(result, new HttpHeaders(), httpStatus);
    }
}

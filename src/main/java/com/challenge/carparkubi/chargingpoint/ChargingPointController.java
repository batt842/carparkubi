package com.challenge.carparkubi.chargingpoint;

import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotActiveException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotFoundException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointUnavailableException;
import com.challenge.carparkubi.chargingpoint.model.ChargingPointPlugged;
import com.challenge.carparkubi.chargingpoint.model.ChargingPointUnplugged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ChargingPointController {
    private static Logger logger = LoggerFactory.getLogger(ChargingPointController.class);

    @Autowired
    private ChargingPointService service;

    @PutMapping("/{id}/plug")
    public ChargingPointPlugged plug(@PathVariable("id") String id) {
        logger.info(id + " sends a notification that a car is plugged");

        try {
            ChargingType type = service.plug(id);
            return new ChargingPointPlugged("OK", type);
        } catch (ChargingPointUnavailableException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resource occupied", e);
        } catch (ChargingPointNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resource not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        } finally {
            logReport();
        }
    }

    @PutMapping("/{id}/unplug")
    public ChargingPointUnplugged unplug(@PathVariable("id") String id) {
        try {
            service.unplug(id);
            return new ChargingPointUnplugged("OK");
        } catch (ChargingPointNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resource not occupied", e);
        } catch (ChargingPointNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resource not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        } finally {
            logReport();
        }
    }

    @GetMapping("/report")
    public LinkedHashMap<String, String> report() {
        LinkedHashMap<String, String> report = service.report();
        logReport(report);
        return report;
    }

    private void logReport() {
        LinkedHashMap<String, String> report = service.report();
        logReport(report);
    }

    private void logReport(LinkedHashMap<String, String> report) {
        StringBuilder sb = new StringBuilder();
        sb.append("Report:\n");

        for (Map.Entry<String, String> e : report.entrySet()) {
            sb.append(e.getKey());
            sb.append(" ");
            sb.append(e.getValue());
            sb.append("\n");
        }

        logger.info(sb.toString());
    }
}

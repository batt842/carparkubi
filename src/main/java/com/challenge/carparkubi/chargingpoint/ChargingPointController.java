package com.challenge.carparkubi.chargingpoint;

import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotActiveException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotFoundException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointShortOfCurrentException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointUnavailableException;
import com.challenge.carparkubi.chargingpoint.model.ChargingPointPlugged;
import com.challenge.carparkubi.chargingpoint.model.ChargingPointUnplugged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("v1/cps")
public class ChargingPointController {
    private static Logger logger = LoggerFactory.getLogger(ChargingPointController.class);

    @Autowired
    private ChargingPointService service;

    @PostMapping("/{id}")
    public ChargingPointPlugged plug(@PathVariable("id") String id) throws ExecutionException, ChargingPointNotFoundException, ChargingPointUnavailableException, ChargingPointShortOfCurrentException {
        logger.info(id + " sends a notification that a car is plugged");

        try {
            ChargingType type = service.plug(id);
            return new ChargingPointPlugged("OK", type);
        } finally {
            logReport();
        }
    }

    @DeleteMapping("/{id}")
    public ChargingPointUnplugged unplug(@PathVariable("id") String id) throws ExecutionException, ChargingPointNotActiveException, ChargingPointNotFoundException {
        try {
            service.unplug(id);
            return new ChargingPointUnplugged("OK");
        } finally {
            logReport();
        }
    }

    @GetMapping("/report")
    public LinkedHashMap<String, String> report() throws ExecutionException {
        LinkedHashMap<String, String> report = service.report();
        logReport(report);
        return report;
    }

    private void logReport() throws ExecutionException {
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

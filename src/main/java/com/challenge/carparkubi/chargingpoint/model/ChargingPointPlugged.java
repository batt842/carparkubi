package com.challenge.carparkubi.chargingpoint.model;

import com.challenge.carparkubi.chargingpoint.ChargingType;

public class ChargingPointPlugged {
    private String result;
    private ChargingType type;

    public ChargingPointPlugged(String result, ChargingType type) {
        this.result = result;
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public ChargingType getType() {
        return type;
    }
}

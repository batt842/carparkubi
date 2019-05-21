package com.challenge.carparkubi.chargingpoint;

import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * In-memory data structure
 * It's very simple and basic so use it with care.
 */
@Repository
public class ChargingPointRepository {
    public static final String ID_PREFIX = "CP";
    public static final int NUMBER_OF_CPS = 10;
    public static final int CURRENT_CAPACITY = 100;
    public static final int FAST_CHARGING_CURRENT = 20;
    public static final int SLOW_CHARGING_CURRENT = 10;

    // Data structure to indicated which CP is occupied or not Simple/ordered
    private Map<String, Boolean> cpOccupancy = new LinkedHashMap<>();

    // List of fast-charging CPs
    private LinkedList<String> fastChargingCps = new LinkedList<>();

    // Set of slow-charging CPs
    private LinkedList<String> slowChargingCps = new LinkedList<>();

    public ChargingPointRepository() {
        // Initialize CPs
        // The number of CPs could be configurable by a property or etc...
        for (int i = 0; i < NUMBER_OF_CPS; i++)
            cpOccupancy.put(ID_PREFIX + (i + 1), Boolean.FALSE);
    }

    public boolean exists(String id) {
        return cpOccupancy.containsKey(id);
    }

    public boolean isCharging(String id) {
        return cpOccupancy.get(id);
    }

    public synchronized void charge(String id, ChargingType type) {
        cpOccupancy.put(id, Boolean.TRUE);
        switch(type) {
            case Fast:
                fastChargingCps.offer(id);
                break;
            case Slow:
                slowChargingCps.offer(id);
                break;
        }
    }

    public synchronized void unplug(String id) {
        cpOccupancy.put(id, Boolean.FALSE);
        if (fastChargingCps.contains(id))
            fastChargingCps.remove(id);
        if (slowChargingCps.contains(id))
            slowChargingCps.remove(id);
    }

    public boolean switchOneFastToSlow() {
        if (fastChargingCps.size() == 0)
            return false;
        slowChargingCps.offer(fastChargingCps.poll());
        return true;
    }

    public boolean switchOneSlowToFast() {
        if (slowChargingCps.size() == 0)
            return false;
        if (CURRENT_CAPACITY - getCurrentCurrent() < FAST_CHARGING_CURRENT - SLOW_CHARGING_CURRENT)
            return false;
        fastChargingCps.offer(slowChargingCps.poll());
        return true;
    }

    public ChargingType getChargingType(String id) {
        if (fastChargingCps.contains(id))
            return ChargingType.Fast;
        if (slowChargingCps.contains(id))
            return ChargingType.Slow;
        return null; // it's not recommended to return null, but...
    }

    public synchronized LinkedHashMap<String, String> getAllStatus() {
        LinkedHashMap<String, String> allStatus = new LinkedHashMap<>();
        for (Map.Entry<String, Boolean> cp : cpOccupancy.entrySet()) {

            String id = cp.getKey();
            Boolean occupied = cp.getValue();

            StringBuilder sb = new StringBuilder();
            if (occupied) {
                sb.append("OCCUPIED ");
                sb.append(getChargingType(id) == ChargingType.Fast ? "20A" : "10A");
            } else
                sb.append("AVAILABLE");

            allStatus.put(id, sb.toString());
        }
        return allStatus;
    }

    public int getCurrentCurrent() {
        return fastChargingCps.size() * FAST_CHARGING_CURRENT
                + slowChargingCps.size() * SLOW_CHARGING_CURRENT;
    }

    public boolean isFastChargingAvailable() {
        return CURRENT_CAPACITY - getCurrentCurrent() >= FAST_CHARGING_CURRENT;
    }

    public boolean isSlowChargingAvailable() {
        return CURRENT_CAPACITY - getCurrentCurrent() >= SLOW_CHARGING_CURRENT;
    }
}

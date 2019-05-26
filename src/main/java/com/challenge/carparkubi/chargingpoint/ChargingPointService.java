package com.challenge.carparkubi.chargingpoint;

import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotActiveException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotFoundException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointShortOfCurrentException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class ChargingPointService {

    @Autowired
    private ChargingPointRepository repository;

    public ChargingPointService() {
    }

    public ChargingPointService(ChargingPointRepository repository) {
        this.repository = repository;
    }

    /**
     * @param id
     * @return
     * @throws ChargingPointUnavailableException
     * @throws ChargingPointNotFoundException
     * @throws ChargingPointShortOfCurrentException
     */
    public ChargingType plug(String id) throws ChargingPointUnavailableException, ChargingPointNotFoundException, ChargingPointShortOfCurrentException, ExecutionException {
        if (!repository.exists(id))
            throw new ChargingPointNotFoundException(id + " is not found.");

        if (repository.isCharging(id))
            throw new ChargingPointUnavailableException(id + " is already occupied.");

        // Switch CPs to slow-charging mode as possible
        while (!repository.isFastChargingAvailable()) {
            if (!repository.switchOneFastToSlow())
                break;
        }

        // Charge
        if (repository.isFastChargingAvailable()) {
            repository.charge(id, ChargingType.Fast);
            return ChargingType.Fast;
        } else if (repository.isSlowChargingAvailable()) {
            repository.charge(id, ChargingType.Slow);
            return ChargingType.Slow;
        } else
            // No more capacity
            // According to the current condition, it will never happen
            throw new ChargingPointShortOfCurrentException("Not enough current capacity");
    }

    /**
     * @param id
     * @throws ChargingPointNotFoundException
     * @throws ChargingPointNotActiveException
     */
    public void unplug(String id) throws ChargingPointNotFoundException, ChargingPointNotActiveException, ExecutionException {
        if (!repository.exists(id))
            throw new ChargingPointNotFoundException(id + " is not found.");

        if (!repository.isCharging(id))
            throw new ChargingPointNotActiveException(id + " is not charging.");

        repository.unplug(id);

        // Switch CPs to fast-charging mode as possible
        while (repository.switchOneSlowToFast()) ;
    }

    public LinkedHashMap<String, String> report() throws ExecutionException {
        return repository.getAllStatus();
    }
}

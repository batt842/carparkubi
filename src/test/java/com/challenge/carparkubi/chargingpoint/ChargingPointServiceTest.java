package com.challenge.carparkubi.chargingpoint;

import com.challenge.carparkubi.chargingpoint.exception.ChargingPointNotFoundException;
import com.challenge.carparkubi.chargingpoint.exception.ChargingPointUnavailableException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ChargingPointServiceTest
 * <p>
 * Test some edge cases
 */
public class ChargingPointServiceTest {

    private ChargingPointService service;
    private ChargingPointRepository repository;

    @Before
    public void setup() {
        repository = new ChargingPointRepository();
        service = new ChargingPointService(repository);
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void plug() throws Exception {
        ChargingType type = service.plug("CP1");
        assertEquals(ChargingType.Fast, type);
    }

    /**
     * Test an exception
     */
    @Test(expected = ChargingPointUnavailableException.class)
    public void plug_occupied() throws Exception {
        service.plug("CP1");
        service.plug("CP1");
    }

    /**
     * Test an exception
     */
    @Test(expected = ChargingPointNotFoundException.class)
    public void plug_notFound() throws Exception {
        service.plug("CP0");
    }

    /**
     * Test a complicated case - which mixture of switch to slow-charging and switch to fast-charging
     */
    @Test
    public void plug_distribute_current() throws Exception {
        service.plug("CP1");
        service.plug("CP2");
        service.plug("CP3");
        service.plug("CP4");
        service.plug("CP5");

        assertEquals(ChargingPointRepository.CURRENT_CAPACITY, repository.getCurrentCurrent());

        service.plug("CP6");

        assertEquals(ChargingType.Slow, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP2"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP3"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP6"));
        assertEquals(ChargingPointRepository.CURRENT_CAPACITY, repository.getCurrentCurrent());

        service.plug("CP7");
        service.plug("CP8");
        service.plug("CP9");
        service.plug("CP10");

        assertEquals(ChargingType.Slow, repository.getChargingType("CP10"));
        assertEquals(ChargingPointRepository.CURRENT_CAPACITY, repository.getCurrentCurrent());
    }

    /**
     * Test if the newest slow-charging CP is switched to fast-charging
     */
    @Test
    public void unplug() throws Exception {
        service.plug("CP1");
        service.plug("CP2");
        service.plug("CP3");
        service.plug("CP4");
        service.plug("CP5");
        service.plug("CP6");
        service.plug("CP7");
        service.plug("CP8");
        service.plug("CP9");
        service.plug("CP10");

        service.unplug("CP7");

        assertEquals(ChargingType.Slow, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP9"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP10"));
    }
}